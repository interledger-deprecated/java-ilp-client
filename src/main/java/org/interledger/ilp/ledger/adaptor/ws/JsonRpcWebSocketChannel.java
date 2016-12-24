package org.interledger.ilp.ledger.adaptor.ws;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonRpcWebSocketChannel implements Closeable {

  protected final Logger log = LoggerFactory.getLogger(this.getClass());

  private ObjectMapper jsonObjectMapper;
  private WebSocketSession session;
  private JsonRpcWebSocketHandler handler;
  private WebSocketConnectionManager connectionManager;
  private JsonRpcRequestResponseMapper responseMapper;

  private boolean isClosing = false;
  private boolean autoReconnect;
  private int maxConnectAttempts;
  private int connectAttempts = 0;

  public JsonRpcWebSocketChannel(URI uri, boolean autoReconnect, int maxConnectAttempts) {
    this.jsonObjectMapper = new ObjectMapper();
    StandardWebSocketClient client = new StandardWebSocketClient();
    this.handler = new JsonRpcWebSocketHandler(this, jsonObjectMapper);
    this.connectionManager = new WebSocketConnectionManager(client, handler, uri.toString());
    this.responseMapper = new JsonRpcRequestResponseMapper(5 * 60 * 1000);
    this.autoReconnect = autoReconnect;
    this.maxConnectAttempts = maxConnectAttempts;
  }

  public void open() {
    connectAttempts++;
    connectionManager.start();
    // TODO Wait for connected event - this is a synchronous operation?
  }

  @Override
  public void close() throws IOException {
    isClosing = true;
    session.close();
    connectionManager.stop();
  }

  public boolean canSend() {
    return session.isOpen();
  }

  public boolean isOpen() {
    return connectionManager.isRunning();
  }

  public void sendRpcRequest(JsonRpcRequestMessage request, JsonRpcResponseHandler responseHandler) {

    if (request.getId() == null && responseHandler != null) {
      throw new IllegalArgumentException(
          "No request ID provided. Unable to process responseHandler.");
    }

    if (request.getId() != null) {
      responseMapper.storeRequest(request, responseHandler);
    }

    try {
      String rpcPayload = jsonObjectMapper.writeValueAsString(request).replaceAll("\r", "").replaceAll("\n", "");
      log.trace("Sending Json Rpc message: " + rpcPayload);
      session.sendMessage(new TextMessage(rpcPayload));
    } catch (JsonProcessingException e) {
      log.error("Error serializing Json Rpc message.", e);
    } catch (IOException e) {
      throw new UncheckedIOException("Error sending message via websocket.", e);
    }
  }

  public void onConnectionEstablished(WebSocketSession session) {
    this.session = session;
    this.connectAttempts = 0;
  }

  public void onMessage(JsonRpcMessage message) {}

  public abstract void onTransportError(Throwable exception);

  public void onConnectionClosed(CloseStatus status) {

    if (autoReconnect && !isClosing && (connectAttempts < maxConnectAttempts)) {

      // Reconnecting
      log.debug(
          "Attempting to reconnect. " + "Attempt " + connectAttempts + " of " + maxConnectAttempts);
      if (!this.session.isOpen()) {
        connectionManager.stop();
      }
      
      connectionManager.start();
      
    }

  }

  public void onResponse(JsonRpcResponseMessage response) {
    if (response.getId() != null) {
      responseMapper.handleResponse(response);
    }
  }

  public JsonRpcWebSocketHandler getHandler() {
    return this.handler;
  }

  public WebSocketSession getSession() {
    return this.session;
  }


}
