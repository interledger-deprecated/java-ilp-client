package org.interledger.ilp.ledger.client;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.core.ledger.LedgerAdaptor;
import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.model.ConnectorInfo;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerErrorEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerMessageEvent;
import org.interledger.ilp.ledger.client.exceptions.ResponseTimeoutException;
import org.interledger.ilp.ledger.client.json.JsonMessageEnvelope;
import org.interledger.ilp.ledger.client.json.JsonQuoteRequest;
import org.interledger.ilp.ledger.client.json.JsonQuoteRequestEnvelope;
import org.interledger.ilp.ledger.client.json.JsonQuoteResponse;
import org.interledger.ilp.ledger.client.model.ClientLedgerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LedgerClient {

  private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);

  //Connectors <name, localAccountId>
  private Map<String, String> connectors;
  
  private LedgerAdaptor adaptor;
  private LedgerEventHandler eventHandler;
  private String account;
  private LedgerMessageResponseMapper messageMapper;
  private ObjectMapper jsonObjectMapper;
  
  private boolean isConnecting = false;
  
  /**
   * Create a new client using the provided adaptor and using the given local ledger account as
   * the default from account.
   *  
   * @param adaptor Ledger adaptor to use for connecting to the ledger
   * @param account Default from account for transfers and messages (the adaptor should be authorized to transact with this account)
   * @param connectors List of connectors that hold accounts on this ledger
   * @param eventHandler An event handler for all {@link LedgerEvent}s that are raised by the client
   */
  public LedgerClient(LedgerAdaptor adaptor, String account, Map<String, String> connectors, LedgerEventHandler eventHandler) {
    this.adaptor = adaptor;
    this.account = account;
    this.connectors = connectors;
    this.eventHandler = new LedgerEventProxy(eventHandler);
    this.messageMapper = new LedgerMessageResponseMapper(60 * 1000);
    this.jsonObjectMapper = new ObjectMapper();
  }  
  
  public void connect() throws Exception {
    
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

  public String getAccount() {
    return this.account;
  }

  //FIXME Should we be exposing this? Maybe some methods required on the Client instead?
  public LedgerAdaptor getAdaptor() {
    return this.adaptor;
  }
  
  public JsonQuoteResponse requestQuote(JsonQuoteRequest query) {

    throwIfNotConnected();
    
    if(query.getSourceAmount() == null ? query.getDestinationAmount() == null : query.getDestinationAmount() != null) {
      throw new IllegalArgumentException("Either the source or destination amount must be provided, but not both.");
    }
    
    if(query.getSourceAddress() == null) {
      query.setSourceAddress(adaptor.getLedgerInfo().getLedgerPrefix() +  account);
    }
    
    //Local transfer
    if(query.getDestinationAddress().startsWith(adaptor.getLedgerInfo().getLedgerPrefix())) {
      String amount = query.getSourceAmount() != null ? query.getSourceAmount() : query.getDestinationAmount();
      
      JsonQuoteResponse response = new JsonQuoteResponse();
      response.setDestinationAmount(amount);
      response.setSourceAmount(amount);
      response.setSourceExpiryDuration(query.getDestinationExpiryDuration());
      
      return response;
    }
    
    //Prepare connector list
    Set<String> connectors = new HashSet<>();
    if(query.getConnectors() != null && query.getConnectors().size() > 0) {
      connectors.addAll(query.getConnectors());
    } else {
      connectors.addAll(this.connectors.values());
    }
    
    SortedSet<JsonQuoteResponse> responses = new ConcurrentSkipListSet<>();
    int connectorCount = connectors.size();
    
    if(connectorCount > 0) {
      //Parallel fetch
      BlockingQueue<String> connectorQueue = 
          new ArrayBlockingQueue<String>(connectorCount, false, connectors);
      ExecutorService es = Executors.newFixedThreadPool(connectorCount);
      for(int count = 0 ; count < connectorCount ; count++) {
          es.submit(new Runnable() {
            
            @Override
            public void run() {
                String connector = null;
                while((connector = connectorQueue.poll()) != null) {
                  try {
                    JsonQuoteResponse response = requestQuoteFromConnector(connector, query);
                    if(response != null) {
                      responses.add(response);
                    }
                  } catch (ResponseTimeoutException e) {
                    log.error("Timed out waiting for quote from " + connector);
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
      //Single fetch
      String connector = connectors.toArray(new String[1])[0];
      try {
        JsonQuoteResponse response = requestQuoteFromConnector(connector, query);
        if(response != null) {
          responses.add(response);
        }
      } catch (ResponseTimeoutException e) {
        log.error("Timed out waiting for quote from " + connector);
      }
    }
    
    if(responses.size() == 0) {
      log.debug("No qutoe responses received in time.");
      return null;
    }
    
    return responses.first();
  }
  
  private JsonQuoteResponse requestQuoteFromConnector(String connector, JsonQuoteRequest query) throws ResponseTimeoutException {
    
    JsonQuoteRequestEnvelope envelope = new JsonQuoteRequestEnvelope();
    envelope.setId(UUID.randomUUID().toString());
    envelope.setData(query);
    
    String messageData;
    try {
      messageData = jsonObjectMapper.writeValueAsString(envelope);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error Serializing Quote Request.", e);
    }
    
    ClientLedgerMessage message = new ClientLedgerMessage();
    message.setFrom(account);
    message.setTo(connector);
    message.setData(messageData.getBytes(Charset.forName("UTF-8")));
    
    BlockingQueue<JsonMessageEnvelope> q = messageMapper.storeRequest(envelope);
    adaptor.sendMessage(message);
    
    //Blocks for 30 seconds - returns null if no response is received
    try {
      JsonMessageEnvelope response = q.poll(30, TimeUnit.SECONDS);
      if(response == null) {
        throw new ResponseTimeoutException("Timed out waiting for quote response.");
      }
      return (JsonQuoteResponse) response.getData();
    } catch (InterruptedException e) {
      throw new RuntimeException("Unexpected interup waiting for quote response.", e);
    }
    
  }
   
  public Object submitQuotedPayment(JsonQuoteResponse quote, String memo, ZonedDateTime expiry, Condition executionCondition) {
    
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
    
    //Subscribe to account events for default account
    try {
      adaptor.subscribeToAccountNotifications(account);
    } catch (Exception e) {
      eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, e));
    }
    
    isConnecting = false;

  }
  
  private void onMessageReceived(LedgerMessage message) {
    if(message.getData() != null) {
      try {
        JsonMessageEnvelope innerMessage = jsonObjectMapper.readValue(message.getData(), JsonMessageEnvelope.class);
        if(innerMessage.getId() != null) {
          messageMapper.handleResponse(innerMessage);          
        }
      } catch (IOException e) {
        // TODO Log parse error
        e.printStackTrace();
      }
    }
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
      } else if (event instanceof ClientLedgerMessageEvent) {
        onMessageReceived(((ClientLedgerMessageEvent) event).getMessage());
      }
    }
  }

  
}
