package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonRpcMessageDeserializer extends StdDeserializer<JsonRpcMessage> {

  private static final long serialVersionUID = -3213662859742573217L;

  protected JsonRpcMessageDeserializer() {
    super(JsonRpcMessage.class);
  }

  @Override
  public JsonRpcMessage deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    
    ObjectMapper mapper = (ObjectMapper) jp.getCodec();  
    ObjectNode root = (ObjectNode) mapper.readTree(jp); 
    
    Class<? extends JsonRpcMessage> rpcClass = null;
    JsonNode methodNode = root.get("method");

    if(methodNode == null || methodNode.isMissingNode() || !methodNode.isValueNode()) {
      rpcClass = JsonRpcResponse.class;
    } else if(methodNode.isValueNode()) {
      String method = methodNode.asText();
      
      if("connect".equals(method)) {
        rpcClass = JsonRpcConnectNotification.class;
      }
      if("notify".equals(method)) {
        rpcClass = JsonRpcNotification.class;
      }
      if("subscribe_account".equals(method)) {
        rpcClass = JsonRpcSubscribeAccountRequest.class;
      }      
    }
    
    if (rpcClass == null) return null;
    return  mapper.readValue(root.traverse(), rpcClass);
  }

}
