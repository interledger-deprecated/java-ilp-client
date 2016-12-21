package org.interledger.ilp.ledger.client;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.core.ledger.LedgerAdaptor;
import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.model.ConnectorInfo;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerErrorEvent;
import org.interledger.ilp.ledger.client.model.ClientLedgerMessage;
import org.interledger.ilp.ledger.client.model.ClientLedgerTransfer;
import org.interledger.ilp.ledger.client.model.ClientQuoteQuery;
import org.interledger.ilp.ledger.client.model.ClientQuoteQueryParams;
import org.interledger.ilp.ledger.client.model.ClientQuoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LedgerClient {

  private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);

  //Connectors <name, localAccountId>
  private Map<String, String> connectors;
  
  private LedgerAdaptor adaptor;
  private LedgerEventHandler eventHandler;
  private String account;
  
  private boolean isConnecting = false;
  
  public LedgerClient(LedgerAdaptor adaptor, String account, Map<String, String> connectors, LedgerEventHandler eventHandler) {
    this.adaptor = adaptor;
    this.account = account;
    this.connectors = connectors;
    this.eventHandler = new LedgerEventProxy(eventHandler);
  }  
  
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

  public ClientQuoteResponse requestQuote(ClientQuoteQueryParams query) throws Exception {

    throwIfNotConnected();
    
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
    if(query.getConnectors() != null && query.getConnectors().size() > 0) {
      connectors.addAll(query.getConnectors());
    } else {
      //TODO Possibly not desired behaviour
      connectors.addAll(this.connectors.values());
    }
    
    //TODO This should be an async parallel operation
    Set<ClientQuoteResponse> responses = new HashSet<>();
    for (String connector : connectors) {
      responses.add(requestQuoteFromConnector(connector, query));
    }
    return findBestQuote(responses);
  }
  
  private ClientQuoteResponse requestQuoteFromConnector(String connector, ClientQuoteQueryParams query) throws Exception {
    
    ClientQuoteQuery quote = new ClientQuoteQuery();
    quote.setData(query);
    quote.setId(UUID.randomUUID().toString());
    quote.setMethod("quote_request");
    
    ClientLedgerMessage message = new ClientLedgerMessage();
    
    //FIXME: we must overcome the horrible, horrible json mapping stuff somehow...
    ObjectMapper mapper = new ObjectMapper();
    message.setData(mapper.writeValueAsString(quote));

    message.setFrom(account);
    message.setTo(connector);
    adaptor.sendMessage(message);
    
    //oh dear, send message is inherently async...
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

  private void onLedgerConnected(ClientLedgerConnectEvent event) {
    
    if(connectors == null || connectors.size() == 0) {
      
      connectors = new HashMap<String, String>();
      
      //Get connectors from ledger if required
      //TODO: Log connectors from ledger that are not in local list (Warning?)
      List<ConnectorInfo> ledgerConnectors;
      try {
        ledgerConnectors = adaptor.getLedgerInfo().getConnectors();
        for (ConnectorInfo connector : ledgerConnectors) {
          connectors.put(connector.getName(), connector.getId());
        }
      } catch (Exception e) {
        eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, e));
      }
    }
    
    //Subscribe to account events
    try {
      adaptor.getAccountService().subscribeToAccountNotifications(account);
    } catch (Exception e) {
      eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, e));
    }
    
    isConnecting = false;

  }
  
  private void throwIfNotConnected() {
    if(isConnecting) {
      throw new RuntimeException("Client is currently connecting.");
    }
    if(!isConnected()) {
      throw new RuntimeException("Client is not connected.");
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
      }      
    }
  }
  
  
}
