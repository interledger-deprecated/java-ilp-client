package org.interledger.ilp.ledger.adaptor.ws;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequest;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRpcWebSocketChannel implements Closeable {
  
  private ObjectMapper mapper;
  private WebSocketSession session;
  private JsonRpcWebSocketHandler handler;
  private WebSocketConnectionManager connectionManager;

  public JsonRpcWebSocketChannel(URI uri) {
    this.mapper = new ObjectMapper();
    StandardWebSocketClient client = new StandardWebSocketClient();
    this.handler = new JsonRpcWebSocketHandler(this, mapper); 
    this.connectionManager = new WebSocketConnectionManager(client, handler, uri.toString());
  }
  
  public void open() {
    connectionManager.start();
  }
  
  @Override
  public void close() throws IOException {
    connectionManager.stop();
  }

  public boolean canSend() {
    return session.isOpen();
  }
  
  public boolean isOpen() {
    return connectionManager.isRunning();
  }
  
 public void sendRpcRequest(JsonRpcRequest request) throws Exception {
    String rpcPayload = mapper.writeValueAsString(request);
    session.sendMessage(new TextMessage(rpcPayload));
  }
  

  public void onConnectionEstablished() {
  }

  public void onJsonRpcMessage(JsonRpcMessage message) {
          }
  
  public void onJsonRpcTransportError(Throwable exception) {
  }
  
  public void onConnectionClosed(CloseStatus status) {
  }
  
  public JsonRpcWebSocketHandler getHandler() {
    return this.handler;
  }
  
  public WebSocketSession getSession() {
    return this.session;
  }

  public void setSession(WebSocketSession session) {
    this.session = session;
  }

}
