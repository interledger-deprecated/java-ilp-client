package org.interledger.ilp.client;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.money.MonetaryAmount;

import org.interledger.cryptoconditions.Condition;
import org.interledger.cryptoconditions.Fulfillment;
import org.interledger.ilp.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.client.events.ClientLedgerErrorEvent;
import org.interledger.ilp.client.events.ClientLedgerMessageEvent;
import org.interledger.ilp.client.exceptions.ResponseTimeoutException;
import org.interledger.ilp.client.model.ClientLedgerMessage;
import org.interledger.ilp.client.model.ClientLedgerTransfer;
import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.InterledgerPaymentRequest;
import org.interledger.ilp.ledger.LedgerAdaptor;
import org.interledger.ilp.ledger.events.LedgerEvent;
import org.interledger.ilp.ledger.events.LedgerEventHandler;
import org.interledger.ilp.ledger.model.AccountInfo;
import org.interledger.ilp.ledger.model.LedgerInfo;
import org.interledger.ilp.ledger.model.LedgerMessage;
import org.interledger.quoting.model.QuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LedgerClient {

  private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);

//  private Set<InterledgerAddress> connectors;
  
  private LedgerAdaptor adaptor;
  private LedgerEventHandler eventHandler;
  private InterledgerAddress clientAccountAddress;
  private LedgerMessageResponseMapper messageMapper;
  
  private boolean isConnecting = false;
  
  /**
   * Create a new client using the provided adaptor and using the given local ledger accountAddress as
   * the default from accountAddress.
   *  
   * @param adaptor Ledger adaptor to use for connecting to the ledger
   * @param accountAddress Default from clientAccountAddress for transfers and messages (the adaptor should be authorized to transact with this clientAccountAddress)
   * @param connectors Connectors that hold accounts on this ledger
   * @param eventHandler An event handler for all {@link LedgerEvent}s that are raised by the client
   */
  public LedgerClient(LedgerAdaptor adaptor, InterledgerAddress accountAddress, LedgerEventHandler eventHandler) {
    this.adaptor = adaptor;
    this.clientAccountAddress = accountAddress;
    this.eventHandler = new LedgerEventProxy(eventHandler);
    this.messageMapper = new LedgerMessageResponseMapper(60 * 1000);
  }  
  
  public void connect() throws Exception {
    log.info("Connecting...");
    
    if(adaptor.isConnected() || isConnecting) {
      throw new Exception("Bad state");
    }
    
    isConnecting = true;
    
    adaptor.setEventHandler(eventHandler);
    adaptor.connect();
    
  }

  public boolean isConnected() {
    return adaptor.isConnected();
  }

  public void disconnect() throws Exception {
    log.info("Disconnecting...");
    adaptor.disconnect();
  }

  public LedgerInfo getLedgerInfo() {
    throwIfNotConnected();
    return this.adaptor.getLedgerInfo();
  }
  
  public InterledgerAddress getAccount() {
    return this.clientAccountAddress;
  }
  
  public Set<InterledgerAddress> getRecommendedConnectors() {
    throwIfNotConnected();
    return adaptor.getConnectors();
  }
  
  public AccountInfo getAccountInfo() {
    throwIfNotConnected();
    return adaptor.getAccountInfo(this.clientAccountAddress);
  }
    
  public LedgerMessage sendMessage(InterledgerAddress to, String type, Object data, int timeout) throws ResponseTimeoutException {
    
    throwIfNotConnected();
    
    UUID id = UUID.randomUUID();
    ClientLedgerMessage message = new ClientLedgerMessage();
    message.setId(id);
    message.setFrom(clientAccountAddress);
    message.setTo(to);
    message.setType(type);
    message.setData(data);
    
    BlockingQueue<LedgerMessage> q = messageMapper.storeRequest(message);
    this.adaptor.sendMessage(message);
    
    //Blocks for 30 seconds - returns null if no response is received
    try {
      LedgerMessage response = q.poll(timeout, TimeUnit.SECONDS);
      if(response == null) {
        throw new ResponseTimeoutException("Timed out waiting for quote response.");
      }
      return response;
    } catch (InterruptedException e) {
      throw new RuntimeException("Unexpected interupt waiting for quote response.", e);
    }
  }
  
  public UUID sendTransfer(InterledgerAddress to, MonetaryAmount amount) {
    return sendTransfer(to, amount, null, null, null);
  }
  
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
    if(executionCondition != null) {
      transfer.setExecutionCondition(executionCondition);
      if(expiresAt == null) {
        throw new IllegalArgumentException("Can't specify an execution condition without a timeout.");
      }
      transfer.setExpiresAt(expiresAt);
    } else {
      if(expiresAt != null) {
        throw new IllegalArgumentException("Can't specify an timeout without an execution condition.");
      }
    }
    transfer.setData(data);
    
    this.adaptor.sendTransfer(transfer);
    return id;
  }

  public void fulfillTransfer(UUID transferId, Fulfillment fulfillment) {
    this.adaptor.fulfillTransfer(transferId, fulfillment);
  }
  
  public UUID makePayment(QuoteResponse quote, InterledgerPaymentRequest paymentRequest) {
    
    //TODO Make sure that the quote matches the IPR
    
    byte[] data = generateTransferMemo(paymentRequest);
    ZonedDateTime expiresAt = calculateLocalExpiry(paymentRequest.getExpiresAt());
    
    return sendTransfer(
        quote.getSourceConnectorAccount(), 
        quote.getSourceAmount(), 
        paymentRequest.getCondition(), 
        expiresAt, 
        data);
        
  }
  
  private byte[] generateTransferMemo(InterledgerPaymentRequest paymentRequest) {
    // TODO Auto-generated method stub
    return null;
  }

  private ZonedDateTime calculateLocalExpiry(ZonedDateTime expiresAt) {
    // TODO This should be a little more sophisticated
    return expiresAt;
  }

  private void onLedgerConnected(ClientLedgerConnectEvent event) {
    
    //Subscribe to clientAccountAddress events for default clientAccountAddress
    try {
      adaptor.subscribeToAccountNotifications(clientAccountAddress);
    } catch (Exception e) {
      eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, e));
    }
    
    isConnecting = false;

  }
  
  private void onMessageReceived(LedgerMessage message) {
    if(message.getId() != null) {
      messageMapper.handleResponse(message);          
    }
  }
  
  private void throwIfNotConnected() {
    if(isConnecting) {
      throw new RuntimeException("LedgerClient is currently connecting.");
    }
    if(!isConnected()) {
      throw new RuntimeException("LedgerClient is not connected.");
    }
  }
  
  private class LedgerEventProxy implements LedgerEventHandler {

    private LedgerEventHandler proxiedHandler;
    
    public LedgerEventProxy(LedgerEventHandler eventHandler) {
      this.proxiedHandler = eventHandler;
    }
    
    @Override
    public void handleLedgerEvent(LedgerEvent event) {
      
      if(proxiedHandler != null) {
        proxiedHandler.handleLedgerEvent(event);
      }
      
      //Internal Logic
      if(event instanceof ClientLedgerConnectEvent) {
        onLedgerConnected((ClientLedgerConnectEvent) event);
      } else if (event instanceof ClientLedgerMessageEvent) {
        onMessageReceived(((ClientLedgerMessageEvent) event).getMessage());
      }
    }
  }

  
}
