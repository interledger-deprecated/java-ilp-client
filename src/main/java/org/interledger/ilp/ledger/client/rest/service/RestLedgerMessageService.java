package org.interledger.ilp.ledger.client.rest.service;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;
import org.interledger.ilp.core.ledger.service.LedgerMessageService;
import org.interledger.ilp.ledger.client.ws.JsonRpcWebSocketClientHandler;
import org.interledger.ilp.ledger.client.ws.JsonRpcWebsocketClient;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;

public class RestLedgerMessageService implements LedgerMessageService {

  private String connectionString;
  private WebSocketHandler handler;
  private WebSocketConnectionManager connectionManager;
  
  public RestLedgerMessageService(String connectionString) {
    this.connectionString = connectionString;
  }
  
  @Override
  public String getConnectionString() {
    return connectionString;
  }
  
  @Override
  public boolean sendNotification(Notification message) {
    // TODO Auto-generated method stub
    return false;
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

}
