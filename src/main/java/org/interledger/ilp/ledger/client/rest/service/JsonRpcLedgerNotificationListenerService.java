package org.interledger.ilp.ledger.client.rest.service;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;
import org.interledger.ilp.core.ledger.service.LedgerNotificationListenerService;
import org.interledger.ilp.ledger.client.ws.JsonRpcWebSocketClientHandler;
import org.interledger.ilp.ledger.client.ws.JsonRpcWebsocketClient;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class JsonRpcLedgerNotificationListenerService implements LedgerNotificationListenerService {

  private String connectionString;
  private WebSocketHandler handler;
  private WebSocketConnectionManager connectionManager;
  
  public JsonRpcLedgerNotificationListenerService(String connectionString) {
    this.connectionString = connectionString;
  }
  
  @Override
  public String getConnectionString() {
    return connectionString;
  }
  
  public void setConnectionString(String connectionString) {
    this.connectionString = connectionString;
  }
  
  @Override
  @EventListener
  public void onNotificationMessageReceived(MessageEvent<Notification> event) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void connect() {
    JsonRpcWebsocketClient client = new JsonRpcWebsocketClient();
    handler = new JsonRpcWebSocketClientHandler();
    connectionManager = new WebSocketConnectionManager(client, handler, connectionString);
    connectionManager.start();
  }

  public boolean isConnected() {
    return connectionManager.isRunning();
  }
  
  @Override
  public void disconnect() throws Exception {
    // TODO Auto-generated method stub
    
  }

}
