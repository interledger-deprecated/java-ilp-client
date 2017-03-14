package org.interledger.ilqp.client;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.client.LedgerClient;
import org.interledger.ilp.client.exceptions.ResponseTimeoutException;
import org.interledger.ilp.client.model.ClientLedgerMessage;
import org.interledger.ilp.exceptions.InterledgerQuotingProtocolException;
import org.interledger.ilp.ledger.model.LedgerMessage;
import org.interledger.ilqp.client.model.ClientQuoteResponse;
import org.interledger.quoting.QuoteSelectionStrategy;
import org.interledger.quoting.QuoteService;
import org.interledger.quoting.model.QuoteErrorResponse;
import org.interledger.quoting.model.QuoteRequest;
import org.interledger.quoting.model.QuoteResponse;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.money.MonetaryAmount;


/**
 * The ILQP service gets quotes by using the ILQP protocol. This involves sending a quote request to
 * neighboring connectors via the ledger interface. As such the service is dependent on the
 * LedgerClient.
 * 
 * @author adrianhopebailie
 *
 */
public class IlqpQuoteService implements QuoteService {

  public static final int DEFAULT_TIMEOUT_SECONDS = 60;

  private LedgerClient client;
  private Set<InterledgerAddress> connectors;
  private int requestTimeoutMillis;

  /**
   * Constructs an instance of the quote service.
   * 
   * @param client A client used to communicate with the ledger.
   */
  public IlqpQuoteService(LedgerClient client) {
    this(client, client.getRecommendedConnectors(),
        (int) TimeUnit.SECONDS.toMillis(DEFAULT_TIMEOUT_SECONDS));
  }

  /**
   * Constructs an instance of the quote service.
   * 
   * @param client A client used to communicate with the ledger.
   * @param connectors A set of connectors that will be queried for quotes.
   * @param requestTimeoutMillis The amount of time to wait for responses to quotes before timing
   *        out.
   */
  public IlqpQuoteService(LedgerClient client, Set<InterledgerAddress> connectors,
      int requestTimeoutMillis) {
    this.client = client;
    this.connectors = connectors;
    this.requestTimeoutMillis = requestTimeoutMillis;
  }

  @Override
  public QuoteResponse requestQuote(QuoteRequest query, QuoteSelectionStrategy selectionStrategy)
      throws InterledgerQuotingProtocolException {

    throwIfClientNotConnected();

    if (query.getSourceAmount() == null ? query.getDestinationAmount() == null
        : query.getDestinationAmount() != null) {
      throw new IllegalArgumentException(
          "Either the source or destination amount must be provided, but not both.");
    }

    // Local transfer
    if (query.getDestinationAddress().startsWith(client.getLedgerInfo().getAddressPrefix())) {
      MonetaryAmount amount =
          query.getSourceAmount() != null ? query.getSourceAmount() : query.getDestinationAmount();

      ClientQuoteResponse response = new ClientQuoteResponse();
      response.setSourceAmount(amount);
      response.setDestinationAmount(amount);
      response.setSourceConnectorAccount(null);
      response.setSourceLedger(client.getLedgerInfo().getAddressPrefix());
      response.setDestinationLedger(client.getLedgerInfo().getAddressPrefix());
      response.setSourceExpiryDuration(query.getDestinationExpiryDuration());
      response.setDestinationExpiryDuration(query.getDestinationExpiryDuration());
      return response;
    }

    Set<QuoteResponse> responses = new ConcurrentSkipListSet<>();
    int connectorCount = connectors.size();

    if (connectorCount == 0) {
      // TODO Throw?
      return null;
    }

    // Parallel fetch
    BlockingQueue<InterledgerAddress> connectorQueue =
        new ArrayBlockingQueue<InterledgerAddress>(connectorCount, false, connectors);
    ExecutorService es = Executors.newFixedThreadPool(connectorCount);
    for (int count = 0; count < connectorCount; count++) {
      es.submit(new Runnable() {

        @Override
        public void run() {
          InterledgerAddress connector = null;
          while ((connector = connectorQueue.poll()) != null) {
            try {
              QuoteResponse response = requestQuote(query, connector);
              if (response != null) {
                responses.add(response);
              } else {
                throw new InterledgerQuotingProtocolException(
                    "Error getting quote from " + connector);
              }
            } catch (InterledgerQuotingProtocolException e) {
              // TODO Log these failures
              e.printStackTrace();
            }
          }
        }
      });
    }
    es.shutdown();
    try {
      es.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException("Unexpected interupt waiting for quote responses.", e);
    }

    if (responses.size() == 0) {
      // Throw?
      return null;
    }

    return selectionStrategy.apply(responses);

  }

  @Override
  public QuoteResponse requestQuote(QuoteRequest query, InterledgerAddress connector)
      throws InterledgerQuotingProtocolException {

    ClientLedgerMessage message = new ClientLedgerMessage();
    message.setId(UUID.randomUUID());
    message.setType("quote_request");
    message.setFrom(client.getAccount());
    message.setTo(connector);
    message.setData(query);

    try {
      LedgerMessage response =
          client.sendMessage(connector, "quote_request", query, requestTimeoutMillis);
      if ("error".equals(response.getType())) {
        QuoteErrorResponse error = (QuoteErrorResponse) response.getData();

        throw new InterledgerQuotingProtocolException(
            "Error getting quote: [" + error.getId() + "]\"" + error.getMessage() + "\".");
      } else if ("quote_response".equals(response.getType())) {
        return (QuoteResponse) response.getData();
      } else {
        throw new InterledgerQuotingProtocolException(
            "Invalid quote response: " + response.getData().toString());
      }
    } catch (ResponseTimeoutException e) {
      throw new InterledgerQuotingProtocolException("Timed out waiting for quote.", e);
    }
  }

  /**
   * Convenience method to check if the client is connected to the ledger an raise an exception if
   * not.
   */
  private void throwIfClientNotConnected() {
    if (!client.isConnected()) {
      throw new RuntimeException("Client is not connected.");
    }
  }
}
