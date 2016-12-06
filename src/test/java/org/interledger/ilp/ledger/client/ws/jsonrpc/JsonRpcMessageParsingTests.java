package org.interledger.ilp.ledger.client.ws.jsonrpc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.UUID;

import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcConnectNotification;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotification;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessageNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestTransferNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponse;
import org.junit.Test;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRpcMessageParsingTests {

  @Test
  public final void parseJsonRpcConnect() throws JsonParseException, JsonMappingException, IOException {
    
    ObjectMapper mapper = new ObjectMapper();
    String message = "{\"jsonrpc\":\"2.0\",\"method\":\"connect\", \"id\":null}";
    JsonRpcMessage rpcMessage = mapper.readValue(message, JsonRpcMessage.class);
    Assert.isTrue(rpcMessage instanceof JsonRpcConnectNotification);
    
  }

  @Test
  public final void parseJsonRpcResponse() throws JsonParseException, JsonMappingException, IOException {
    
    ObjectMapper mapper = new ObjectMapper();
    String message = "{\"jsonrpc\":\"2.0\",\"result\":1, \"id\":\"f0e7f8e5-d843-4d34-8961-adf27e0e90b0\"}";
    JsonRpcMessage rpcMessage = mapper.readValue(message, JsonRpcMessage.class);
    Assert.isTrue(rpcMessage instanceof JsonRpcResponse);
    
  }

  @Test
  public final void parseJsonRpcMessageNotificationRequest() throws JsonParseException, JsonMappingException, IOException {
    
    ObjectMapper mapper = new ObjectMapper();
    String message = "{"
        + "\"jsonrpc\":\"2.0\","
        + "\"method\":\"notify\","
        + "\"id\": null, "
        + "\"params\":{"
          + "\"event\":\"message.send\","
          + "\"id\":\"" + UUID.randomUUID() + "\", "
          + "\"resource\": {"
            + "\"from\":\"adrian\","
            + "\"to\":\"andrew\","
            + "\"data\":\"message\","
            + "\"ledger\":\"ledger.example\""
          + "}"
        + "}"
      + "}";
    JsonRpcMessage rpcMessage = mapper.readValue(message, JsonRpcMessage.class);
    Assert.isTrue(rpcMessage instanceof JsonRpcNotification);
    JsonRpcNotification rpcNotification = (JsonRpcNotification) rpcMessage;
    
    Assert.isInstanceOf(JsonRpcRequestMessageNotificationParams.class, rpcNotification.getParams());
    JsonRpcRequestMessageNotificationParams params = (JsonRpcRequestMessageNotificationParams) rpcNotification.getParams();
    
    assertEquals(params.getMessage().getData(), "message"); 
    
  }

  @Test
  public final void parseJsonRpcTransferNotificationRequest() throws JsonParseException, JsonMappingException, IOException {
    
    
    ObjectMapper mapper = new ObjectMapper();
    String message = "{"
        + "\"jsonrpc\":\"2.0\","
        + "\"method\":\"notify\","
        + "\"id\": null, "
        + "\"params\":{"
          + "\"event\":\"transfer.create\","
          + "\"id\":\"" + UUID.randomUUID() + "\", "
          + "\"resource\": {"
            + "\"id\":\"adrian\","
            + "\"ledger\":\"andrew\","
            + "\"amount\":\"1000\""
          + "}"
        + "}"
      + "}";
    
    JsonRpcMessage rpcMessage = mapper.readValue(message, JsonRpcMessage.class);
    Assert.isInstanceOf(JsonRpcNotification.class, rpcMessage);
    JsonRpcNotification rpcNotification = (JsonRpcNotification) rpcMessage;
    
    Assert.isInstanceOf(JsonRpcRequestTransferNotificationParams.class, rpcNotification.getParams());
    JsonRpcRequestTransferNotificationParams params = (JsonRpcRequestTransferNotificationParams) rpcNotification.getParams();
    
    assertEquals(params.getTransfer().getId(), "adrian"); 
  }

}
