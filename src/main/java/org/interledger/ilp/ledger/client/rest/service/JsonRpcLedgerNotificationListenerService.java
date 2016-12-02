package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;
import java.util.Map;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;
import org.interledger.ilp.core.ledger.service.LedgerNotificationListenerService;
import org.interledger.ilp.ledger.client.ws.JsonRpcWebSocketClientHandler;
import org.interledger.ilp.ledger.client.ws.JsonRpcWebsocketClient;
import org.interledger.ilp.ledger.client.ws.jsonrpc.JsonRpcRequest;
import org.interledger.ilp.ledger.client.ws.jsonrpc.SubscribeRpcCallFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class JsonRpcLedgerNotificationListenerService implements LedgerNotificationListenerService {

  public static final String WEBSOCKET_URL_NAME = "websocket";
  
  protected Map<String, URI> urls;
  private JsonRpcWebSocketClientHandler handler;
  private WebSocketConnectionManager connectionManager;
  
  public JsonRpcLedgerNotificationListenerService(Map<String, URI> urls,
      ApplicationEventPublisher publisher) {
    this.urls = urls;
    this.handler = new JsonRpcWebSocketClientHandler(publisher);    
  }
  
  @Override
  @EventListener
  public void onNotificationMessageReceived(MessageEvent<Notification> event) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void connect() {
    URI wsUri = urls.get(WEBSOCKET_URL_NAME);
    if(wsUri == null || wsUri.getScheme() == null  || !wsUri.getScheme().startsWith("ws")) {
      throw new RuntimeException("Invalid websocket URL: " + wsUri);
    }

    JsonRpcWebsocketClient client = new JsonRpcWebsocketClient();
    connectionManager = new WebSocketConnectionManager(client, handler, wsUri.toString());
    connectionManager.start();
  }

  public boolean isConnected() {
    return connectionManager.isRunning();
  }
  
  @Override
  public void disconnect() throws Exception {
    connectionManager.stop();
  }

  @Override
  public void subscribeToAccountNotifications(String account) throws Exception {
    if (!isConnected()) {
      throw new Exception("Client is not connected.");
    }
    
    JsonRpcRequest subReq = SubscribeRpcCallFactory.build(this.urls.get("account"), "admin");
    handler.sendRpcRequest(subReq);
  }

}
