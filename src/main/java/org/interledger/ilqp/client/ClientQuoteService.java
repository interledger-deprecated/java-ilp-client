package org.interledger.ilqp.client;

import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.money.MonetaryAmount;

import org.interledger.ilp.client.LedgerClient;
import org.interledger.ilp.client.exceptions.ResponseTimeoutException;
import org.interledger.ilp.client.model.ClientLedgerMessage;
import org.interledger.ilp.core.InterledgerAddress;
import org.interledger.ilp.core.exceptions.InterledgerQuotingProtocolException;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilqp.client.model.ClientQuoteResponse;
import org.interledger.ilqp.core.QuoteService;
import org.interledger.ilqp.core.model.QuoteErrorResponse;
import org.interledger.ilqp.core.model.QuoteRequest;
import org.interledger.ilqp.core.model.QuoteResponse;

public class ClientQuoteService implements QuoteService {
  
  private LedgerClient client;
  private Set<InterledgerAddress> connectors;
  private int requestTimeout;
  
  public ClientQuoteService(LedgerClient client) {
    this(client, client.getRecommendedConnectors(), 60 * 1000);
  }

  public ClientQuoteService(LedgerClient client, Set<InterledgerAddress> connectors, int requestTimeout) {
    this.client = client;
    this.connectors = connectors;
    this.requestTimeout = requestTimeout;
  }

  //TODO Allow user to pass in a BestQuoteResolutionStrategy
  public QuoteResponse requestQuote(QuoteRequest query) throws InterledgerQuotingProtocolException {

    throwIfClientNotConnected();
    
    if(query.getSourceAmount() == null ? query.getDestinationAmount() == null : query.getDestinationAmount() != null) {
      throw new IllegalArgumentException("Either the source or destination amount must be provided, but not both.");
    }
        
    //Local transfer
    if(query.getDestinationAddress().startsWith(client.getLedgerInfo().getAddressPrefix())) {
      MonetaryAmount amount = query.getSourceAmount() != null ? query.getSourceAmount() : query.getDestinationAmount();
      
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
    
    SortedSet<QuoteResponse> responses = new ConcurrentSkipListSet<>();
    int connectorCount = connectors.size();
    
    if (connectorCount == 0) {
      //TODO Throw?
      return null;
    }
    
    if(connectorCount > 1) {
      //Parallel fetch
      BlockingQueue<InterledgerAddress> connectorQueue = 
          new ArrayBlockingQueue<InterledgerAddress>(connectorCount, false, connectors);
      ExecutorService es = Executors.newFixedThreadPool(connectorCount);
      for(int count = 0 ; count < connectorCount ; count++) {
          es.submit(new Runnable() {
            
            @Override
            public void run() {
              InterledgerAddress connector = null;
                while((connector = connectorQueue.poll()) != null) {
                    try {
                      QuoteResponse response = requestQuote(query, connector);
                      if(response != null) {
                        responses.add(response);
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
    }
    else
    {
      //TODO: do we really want a separate block to do the same as above? Is the cost of setting up 
      //an executor and separate thread high enough to justify maintaining identical code blocks?
      
      //Single fetch
      InterledgerAddress connector = connectors.toArray(new InterledgerAddress[1])[0];
        QuoteResponse response = requestQuote(query, connector);
        if(response != null) {
          responses.add(response);
        }
    }
    
    if(responses.size() == 0) {
      //Throw?
      return null;
    }
    
    return responses.first();
  }

  public QuoteResponse requestQuote(QuoteRequest query, InterledgerAddress connector) throws InterledgerQuotingProtocolException {
    
    ClientLedgerMessage message = new ClientLedgerMessage();
    message.setId(UUID.randomUUID());
    message.setType("quote_request");
    message.setFrom(client.getAccount());
    message.setTo(connector);
    message.setData(query);
    
    try {
      LedgerMessage response = client.sendMessage(connector, "quote_request", query, requestTimeout);
      if("error".equals(response.getType())) {
        QuoteErrorResponse error = (QuoteErrorResponse) response.getData();
        
        throw new InterledgerQuotingProtocolException(
            "Error getting quote: [" + error.getId() + "]\"" + error.getMessage() + "\".");
      }
      else if("quote_response".equals(response.getType())) {
        return (QuoteResponse) response.getData();
      }
      else {
        throw new InterledgerQuotingProtocolException(
            "Invalid quote response: " + response.getData().toString());
      }
    } catch (ResponseTimeoutException e) {
      throw new InterledgerQuotingProtocolException("Timed out waiting for quote.", e);
    }    
  }

  private void throwIfClientNotConnected() {
    if(!client.isConnected()) {
      throw new RuntimeException("Client is not connected.");
    }
  }
}
