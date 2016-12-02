package org.interledger.ilp.ledger.client.ws.jsonrpc;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class JsonRpcDeserializer extends StdDeserializer<JsonRpcBase> {

  public JsonRpcDeserializer() {
    super(JsonRpcBase.class);
  }

  @Override
  public JsonRpcBase deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    
    ObjectMapper mapper = (ObjectMapper) p.getCodec();
    TreeNode root = mapper.readTree(p);
    Class<? extends JsonRpcBase> rpcClass = null;
    
    Iterator<String> fieldNameItr = root.fieldNames();
    
    while(fieldNameItr.hasNext()) {
      String fieldName = fieldNameItr.next();
      if ("method".equals(fieldName)) {
        rpcClass = JsonRpcRequest.class;
        break;
      }
      else if ("result".equals(fieldName) || "error".equals(fieldName)) {
        rpcClass = JsonRpcResponse.class;
        break;
      }
    }
      
    if (rpcClass == null) return null;
    return mapper.readValue(root.traverse(), rpcClass);  
   }



}
