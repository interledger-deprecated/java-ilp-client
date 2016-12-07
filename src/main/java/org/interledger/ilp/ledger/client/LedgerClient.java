package org.interledger.ilp.ledger.client;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.core.ledger.LedgerAdaptor;
import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.ledger.client.events.ApplicationEventPublishingLedgerEventHandler;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerErrorEvent;
import org.interledger.ilp.ledger.client.model.ClientLedgerTransfer;
import org.interledger.ilp.ledger.client.model.ClientQuoteQuery;
import org.interledger.ilp.ledger.client.model.ClientQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;

public class LedgerClient implements ApplicationEventPublisherAware {

  private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);

  private Map<String, String> connectors;
  
  private LedgerAdaptor adaptor;
  private LedgerEventHandler eventHandler;
  private String account;
  
  private boolean isConnecting = false;
  
  public void connect() throws Exception {
    
    if(adaptor.isConnected() || isConnecting) {
      throw new Exception("Bad state");
    }
    
    isConnecting = true;
    
    //TODO Allow implementations to pass in another event handler
    adaptor.setEventHandler(eventHandler);
    adaptor.connect();
    
  }

  public boolean isConnected() {
    return adaptor.isConnected();
  }

  public void disconnect() throws Exception {
    log.info("Disconnecting...");
    
  }

  public ClientQuoteResponse requestQuote(ClientQuoteQuery query) throws Exception {

    // TODO validate
    
    //Local transfer
    if(query.getDestinationAddress().startsWith(adaptor.getLedgerInfo().getLedgerPrefix())) {
      String amount = query.getSourceAmount() != null ? query.getSourceAmount() : query.getDestinationAmount();
      
      ClientQuoteResponse response = new ClientQuoteResponse();
      response.setDestinationAddress(query.getDestinationAddress());
      response.setDestinationAmount(amount);
      response.setSourceAddress(query.getSourceAddress());
      response.setSourceAmount(amount);
      response.setDestinationExpiryDuration(query.getDestinationExpiryDuration());
      
      return response;
    }
    
    Set<String> connectors = new HashSet<>();
    if(query.getConnectors() != null && query.getConnectors().size() > 1) {
      connectors.addAll(query.getConnectors());
    } else {
      connectors.addAll(this.connectors.values());
    }
    
    //TODO This should be an async parallel operation
    Set<ClientQuoteResponse> responses = new HashSet<>();
    for (String connector : connectors) {
      responses.add(requestQuoteFromConnector(connector, query));
    }
    return findBestQuote(responses);
  }
  
  private ClientQuoteResponse requestQuoteFromConnector(String connector, ClientQuoteQuery query) {
    //TODO Implement
    return null;
  }
  
  private ClientQuoteResponse findBestQuote(Set<ClientQuoteResponse> responses) {
    //TODO Implement
    return null;
  }
   
  public Object submitQuotedPayment(ClientQuoteResponse quote, String memo, ZonedDateTime expiry, Condition executionCondition, String transferId) {
    
    ClientLedgerTransfer transfer = new ClientLedgerTransfer();
    transfer.setId(transferId);
    return null;
  }

  @EventListener
  public void onLedgerEvent(LedgerEvent event) {
    if(event instanceof ClientLedgerConnectEvent) {
      isConnecting = false;
      
      //Subscribe to account events
      try {
        adaptor.getAccountService().subscribeToAccountNotifications(account);
      } catch (Exception e) {
        eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, e));
        e.printStackTrace();
      }
      
    }
    
  }
  
  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventHandler = new ApplicationEventPublishingLedgerEventHandler(applicationEventPublisher);
  }
  
  
}
