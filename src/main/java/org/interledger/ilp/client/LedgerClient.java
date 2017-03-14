package org.interledger.ilp.client;

import org.interledger.cryptoconditions.Condition;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.InterledgerPaymentRequest;
import org.interledger.ilp.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.client.events.ClientLedgerErrorEvent;
import org.interledger.ilp.client.events.ClientLedgerMessageEvent;
import org.interledger.ilp.client.exceptions.ResponseTimeoutException;
import org.interledger.ilp.client.model.ClientLedgerMessage;
import org.interledger.ilp.client.model.ClientLedgerTransfer;
import org.interledger.ilp.ledger.LedgerAdaptor;
import org.interledger.ilp.ledger.events.LedgerEvent;
import org.interledger.ilp.ledger.events.LedgerEventHandler;
import org.interledger.ilp.ledger.model.AccountInfo;
import org.interledger.ilp.ledger.model.LedgerInfo;
import org.interledger.ilp.ledger.model.LedgerMessage;
import org.interledger.quoting.model.QuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.money.MonetaryAmount;


/**
 * A LedgerClient provides a high level entry point onto a ledger. It makes use of an underlying
 * {@link LedgerAdaptor} to manage communication with a ledger.
 */
public class LedgerClient {

  private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);

  // private Set<InterledgerAddress> connectors;

  private LedgerAdaptor adaptor;
  private LedgerEventHandler eventHandler;
  private InterledgerAddress clientAccountAddress;
  private LedgerMessageResponseMapper messageMapper;

  private boolean isConnecting = false;

  /**
   * Create a new client using the provided adaptor and using the given local ledger accountAddress
   * as the default from accountAddress.
   * 
   * @param adaptor Ledger adaptor to use for connecting to the ledger.
   * @param accountAddress Default from clientAccountAddress for transfers and messages (the adaptor
   *        should be authorized to transact with this clientAccountAddress).
   * @param eventHandler An event handler for all {@link LedgerEvent}s that are raised by the
   *        client.
   */
  public LedgerClient(LedgerAdaptor adaptor, InterledgerAddress accountAddress,
      LedgerEventHandler eventHandler) {
    this.adaptor = adaptor;
    this.clientAccountAddress = accountAddress;
    this.eventHandler = new LedgerEventProxy(eventHandler);
    this.messageMapper = new LedgerMessageResponseMapper(60 * 1000);
  }

  /**
   * Begins the process of establishing a connection to the ledger. Note that this method is
   * typically non-blocking. A connect event will be raised once the connection has been
   * established.
   */
  public void connect() throws Exception {
    log.info("Connecting...");

    if (adaptor.isConnected() || isConnecting) {
      throw new Exception("Bad state");
    }

    isConnecting = true;

    adaptor.setEventHandler(eventHandler);
    adaptor.connect();
  }

  /**
   * Tests whether the client is connected to the ledger.
   */
  public boolean isConnected() {
    return adaptor.isConnected();
  }

  /**
   * Disconnects from the ledger.
   */
  public void disconnect() throws Exception {
    log.info("Disconnecting...");
    adaptor.disconnect();
  }

  /**
   * Returns information about the ledger that the client is connected to.
   */
  public LedgerInfo getLedgerInfo() {
    throwIfNotConnected();
    return adaptor.getLedgerInfo();
  }

  /**
   * Returns the account that this client is configured to use on the ledger.
   */
  public InterledgerAddress getAccount() {
    return clientAccountAddress;
  }

  /**
   * Returns a set of accounts known to be associated with connectors on the ledger.
   */
  public Set<InterledgerAddress> getRecommendedConnectors() {
    throwIfNotConnected();
    return adaptor.getConnectors();
  }

  /**
   * Returns the details of the account this client is configured to use on the ledger.
   */
  public AccountInfo getAccountInfo() {
    throwIfNotConnected();
    return adaptor.getAccountInfo(this.clientAccountAddress);
  }

  /**
   * Sends a message to an ILP address via the ledger.
   * 
   * @param to The ILP address of the message recipient.
   * @param type The type of message, for example 'quote_request'
   * @param data The message data.
   * @param timeoutSeconds The number of seconds to wait for a response before timing out.
   * @return The response received, if any.
   */
  public LedgerMessage sendMessage(InterledgerAddress to, String type, Object data,
      int timeoutSeconds) throws ResponseTimeoutException {

    throwIfNotConnected();

    UUID id = UUID.randomUUID();
    ClientLedgerMessage message = new ClientLedgerMessage();
    message.setId(id);
    message.setFrom(clientAccountAddress);
    message.setTo(to);
    message.setType(type);
    message.setData(data);

    BlockingQueue<LedgerMessage> messageQueue = messageMapper.storeRequest(message);
    adaptor.sendMessage(message);

    try {
      LedgerMessage response = messageQueue.poll(timeoutSeconds, TimeUnit.SECONDS);
      if (response == null) {
        throw new ResponseTimeoutException("Timed out waiting for quote response.");
      }
      return response;
    } catch (InterruptedException e) {
      throw new RuntimeException("Unexpected interupt waiting for quote response.", e);
    }
  }

  /**
   * Sends a transfer through the ledger.
   * 
   * @param to The account to which funds will be transferred.
   * @param amount The amount to transfer.
   * @return The unique identifier of the transfer.
   */
  public UUID sendTransfer(InterledgerAddress to, MonetaryAmount amount) {
    return sendTransfer(to, amount, null, null, null);
  }

  /**
   * Sends a transfer through the ledger.
   * 
   * @param to The account to which funds will be transferred.
   * @param amount The amount to transfer.
   * @param executionCondition The condition under which the transfer will be executed.
   * @param expiresAt The date when the transfer expires and will be rejected by the ledger.
   * @param data Data associated with the transfer.
   * @return The unique identifier of the transfer.
   */
  public UUID sendTransfer(InterledgerAddress to, MonetaryAmount amount,
      Condition executionCondition, ZonedDateTime expiresAt, byte[] data) {

    throwIfNotConnected();

    UUID id = UUID.randomUUID();
    ClientLedgerTransfer transfer = new ClientLedgerTransfer();
    transfer.setId(id);
    transfer.setAmount(amount);
    transfer.setFromAccount(clientAccountAddress);
    transfer.setToAccount(to);
    transfer.setAuthorized(true);
    if (executionCondition != null) {
      transfer.setExecutionCondition(executionCondition);
      if (expiresAt == null) {
        throw new IllegalArgumentException(
            "Can't specify an execution condition without a timeout.");
      }
      transfer.setExpiresAt(expiresAt);
    } else {
      if (expiresAt != null) {
        throw new IllegalArgumentException(
            "Can't specify an timeout without an execution condition.");
      }
    }
    transfer.setData(data);

    adaptor.sendTransfer(transfer);
    return id;
  }

  /**
   * Fulfills a transfer.
   * 
   * @param transferId The unique identifier of the transfer.
   * @param fulfillment The cryptographic fulfillment of transfer.
   */
  public void fulfillTransfer(UUID transferId, Fulfillment fulfillment) {
    adaptor.fulfillTransfer(transferId, fulfillment);
  }

  /**
   * Transfers funds based on the response to a quote and the associated payment request.
   * 
   * @param quote The quote response received.
   * @param paymentRequest The payment request.
   * @return A unique identifier for the transfer.
   */
  public UUID makePayment(QuoteResponse quote, InterledgerPaymentRequest paymentRequest) {

    // TODO Make sure that the quote matches the IPR

    byte[] data = generateTransferMemo(paymentRequest);
    ZonedDateTime expiresAt = calculateLocalExpiry(paymentRequest.getExpiresAt());

    return sendTransfer(quote.getSourceConnectorAccount(), quote.getSourceAmount(),
        paymentRequest.getCondition(), expiresAt, data);
  }

  private byte[] generateTransferMemo(InterledgerPaymentRequest paymentRequest) {
    // TODO Auto-generated method stub
    return null;
  }

  private ZonedDateTime calculateLocalExpiry(ZonedDateTime expiresAt) {
    // TODO This should be a little more sophisticated
    return expiresAt;
  }

  /**
   * Event handler method for the adaptor connecting to the ledger.
   */
  private void onLedgerConnected(ClientLedgerConnectEvent event) {
    try {
      adaptor.subscribeToAccountNotifications(clientAccountAddress);
    } catch (Exception e) {
      eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, e));
    }

    isConnecting = false;
  }

  /**
   * Event handler method for receiving messages.
   */
  private void onMessageReceived(LedgerMessage message) {
    if (message.getId() != null) {
      messageMapper.handleResponse(message);
    }
    // TODO: do we need to think about receiving messages without an id?
  }

  /**
   * Convenience method to test that the client is connected to the ledger.
   */
  private void throwIfNotConnected() {
    if (isConnecting) {
      throw new RuntimeException("LedgerClient is currently connecting.");
    }
    if (!isConnected()) {
      throw new RuntimeException("LedgerClient is not connected.");
    }
  }

  /**
   * Proxies an event handler so that certain events can be handled by the client in addition to an
   * event handler.
   */
  private class LedgerEventProxy implements LedgerEventHandler {

    private LedgerEventHandler proxiedHandler;

    /**
     * Constructs a LedgerEventProxy.
     * 
     * @param eventHandler The event handler to proxy / decorate.
     */
    public LedgerEventProxy(LedgerEventHandler eventHandler) {
      this.proxiedHandler = eventHandler;
    }

    @Override
    public void handleLedgerEvent(LedgerEvent event) {

      if (proxiedHandler != null) {
        proxiedHandler.handleLedgerEvent(event);
      }

      // Internal Logic
      if (event instanceof ClientLedgerConnectEvent) {
        onLedgerConnected((ClientLedgerConnectEvent) event);
      } else if (event instanceof ClientLedgerMessageEvent) {
        onMessageReceived(((ClientLedgerMessageEvent) event).getMessage());
      }
    }
  }


}
