package org.interledger.ilp.ledger.client;

import java.util.Map;

import org.interledger.ilp.core.ledger.LedgerAdaptor;
import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.ledger.client.events.ApplicationEventPublishingLedgerEventHandler;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.event.EventListener;

public class LedgerClient implements ApplicationEventPublisherAware {

  private Map<String, String> connectors;
  private LedgerAdaptor adaptor;
  private LedgerEventHandler eventHandler;
  private ApplicationEventPublisher applicationEventPublisher;

  private boolean isConnecting = false;
  
  public void connect() throws Exception {
    
    if(adaptor.isConnected() || isConnecting) {
      throw new Exception("Bad state");
    }
    
    isConnecting = true;
    
    //TODO Allow implementations to pass in another event handler
    adaptor.setEventHandler(new ApplicationEventPublishingLedgerEventHandler(this.applicationEventPublisher));
    adaptor.connect();
        
    
  }

  public boolean isConnected() {
    return adaptor.isConnected();
  }

  public void disconnect() throws Exception {
    // TODO Auto-generated method stub
    
  }

  public Object requestQuote() {
    // TODO Auto-generated method stub
    return null;
  }
  
  public Object submitQuotedPayment() {
    // TODO Auto-generated method stub
    return null;
  }

  @EventListener
  public void onLedgerEvent(LedgerEvent event) {
    if(event instanceof ClientLedgerConnectEvent) {
      isConnecting = false;
    }
    
  }
  
  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }
  
  
}
