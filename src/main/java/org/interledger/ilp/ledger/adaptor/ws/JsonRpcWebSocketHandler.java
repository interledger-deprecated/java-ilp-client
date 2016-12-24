package org.interledger.ilp.ledger.adaptor.ws;

import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRpcWebSocketHandler extends TextWebSocketHandler {
  
  private static final Logger logger = LoggerFactory.getLogger(JsonRpcWebSocketHandler.class);  
  
  private final ObjectMapper mapper;
  private JsonRpcWebSocketChannel channel;
  
  public JsonRpcWebSocketHandler(JsonRpcWebSocketChannel channel, ObjectMapper mapper) {
    this.mapper = mapper;
    this.channel = channel;
  }
    
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.debug("Opening session id {} ", session.getId()); 
    this.channel.onConnectionEstablished(session);
    super.afterConnectionEstablished(session);
  }
  
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    logger.trace("Json Rpc message received: " + message.getPayload());
    try {
      JsonRpcMessage rpcMessage = mapper.readValue(message.getPayload(), JsonRpcMessage.class);
      this.channel.onMessage(rpcMessage);
    } catch (JsonProcessingException e) {
      logger.error("Invalid json-rpc message received:\n {}", message.getPayload(), e);
      throw e;
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    logger.error("Error in session id {}", session.getId(), exception); 
    this.channel.onTransportError(exception);
    super.handleTransportError(session, exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    logger.debug("Closing session id {}, close status is {} ", session.getId(), status); 
    this.channel.onConnectionClosed(status);
    super.afterConnectionClosed(session, status);
  }

}
