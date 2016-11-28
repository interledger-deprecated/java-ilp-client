package org.interledger.ilp.ledger.client.ws;

import org.interledger.ilp.ledger.client.ws.jsonrpc.JsonRpcBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRpcWebSocketClientHandler extends TextWebSocketHandler {
  
  private static final Logger logger = LoggerFactory.getLogger(JsonRpcWebSocketClientHandler.class);  
  
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.debug("Opening session id {} ", session.getId()); 
    super.afterConnectionEstablished(session);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    
    logger.debug("Message received: " + message.getPayload());
    
    ObjectMapper mapper = new ObjectMapper();
    try {
      JsonRpcBase rpcMessage = mapper.readValue(message.getPayload(), JsonRpcBase.class);
      if(rpcMessage.getMethod().equals("notify")){
        logger.info("Notification received: " + rpcMessage.getParams().toString());

        //TODO Dispatch a notification event
        
      }
      
    } catch (JsonProcessingException e) {
      logger.error("Invalid JsonRpc message received:\n" + message.getPayload());
      throw e;
    }
        
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    logger.error("Error in session " + session.getId(), exception); 
    super.handleTransportError(session, exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    logger.debug("Closing session id {}, close status is {} ", session.getId(), status); 
    super.afterConnectionClosed(session, status);
  }

}
