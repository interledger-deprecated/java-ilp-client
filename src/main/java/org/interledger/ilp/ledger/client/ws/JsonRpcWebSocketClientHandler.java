package org.interledger.ilp.ledger.client.ws;

import org.interledger.ilp.ledger.client.events.LedgerNotificationEvent;
import org.interledger.ilp.ledger.client.ws.jsonrpc.JsonRpcBase;
import org.interledger.ilp.ledger.client.ws.jsonrpc.JsonRpcDeserializer;
import org.interledger.ilp.ledger.client.ws.jsonrpc.JsonRpcRequest;
import org.interledger.ilp.ledger.client.ws.jsonrpc.JsonRpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JsonRpcWebSocketClientHandler extends TextWebSocketHandler {
  
  private static final Logger logger = LoggerFactory.getLogger(JsonRpcWebSocketClientHandler.class);  
  
  private final ApplicationEventPublisher publisher;
  private final ObjectMapper mapper;
  
  private WebSocketSession session;
  
  public JsonRpcWebSocketClientHandler(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
    
    //TODO: this doesnt belong here, but for now...
    this.mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule("JsonRpc", new Version(1, 0, 0, null, null, null));
    module.addDeserializer(JsonRpcBase.class, new JsonRpcDeserializer());
    this.mapper.registerModule(module);
  }
  
  public void sendRpcRequest(JsonRpcRequest subReq) throws Exception {
    String rpcPayload = mapper.writeValueAsString(subReq);
    
    logger.debug("sending json rpc payload {}", rpcPayload);
    
    session.sendMessage(new TextMessage(rpcPayload));
  }  
  
  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    logger.debug("Opening session id {} ", session.getId()); 
    this.session = session;
    super.afterConnectionEstablished(session);
  }
  
  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    logger.debug("Message received: " + message.getPayload());

    try {
      JsonRpcBase rpcMessage = mapper.readValue(message.getPayload(), JsonRpcBase.class);

      if (rpcMessage instanceof JsonRpcResponse) {
        handleJsonRpcResponse((JsonRpcResponse) rpcMessage);
      } else if (rpcMessage instanceof JsonRpcRequest) {
        handleJsonRpcRequest((JsonRpcRequest) rpcMessage);
      }
    } catch (JsonProcessingException e) {
      logger.error("Invalid json-rpc message received:\n {}", message.getPayload(), e);
      throw e;
    }
  }

  protected void handleJsonRpcRequest(JsonRpcRequest rpcRequest) {
    // TODO: maybe there is a better way to handle this one? the ledger sends a 'connect' rpc
    // call, which has no value in propagating (at least, not that i can see...
    if ("connect".equals(rpcRequest.getMethod())) {
      return;
    }

    // TODO: we should probably abstract this into a separate handler to deal with the various
    // events
    if ("notify".equals(rpcRequest.getMethod())) {
      logger.info("Notification received: " + rpcRequest.getParams().toString());
      publisher.publishEvent(new LedgerNotificationEvent("ledger", null));
    } else {
      logger.warn("received unknown json-rpc call. id {}, method {}", rpcRequest.getId(),
          rpcRequest.getMethod());
    }
  }

  protected void handleJsonRpcResponse(JsonRpcResponse rpcResponse) {
    if (rpcResponse.getError() != null) {
      logger.warn("error in json-rpc request for id {}. error code {}, message {}, data {} ",
          rpcResponse.getId(), rpcResponse.getError().getCode(),
          rpcResponse.getError().getMessage(), rpcResponse.getError().getData());
    }
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    logger.error("Error in session id {}", session.getId(), exception); 
    super.handleTransportError(session, exception);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    logger.debug("Closing session id {}, close status is {} ", session.getId(), status); 
    super.afterConnectionClosed(session, status);
  }
}
