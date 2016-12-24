package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=JsonRpcResponseMessage.class)
public class JsonRpcResponseMessage extends JsonRpcMessage {

  private Object result;
  private JsonRpcError error;

  @JsonProperty(value = "result")
  public Object getResult() {
    return result;
  }

  @JsonProperty(value = "error")
  public JsonRpcError getError() {
    return error;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public void setError(JsonRpcError error) {
    this.error = error;
  }
  
  public static boolean isSuccess(JsonRpcResponseMessage response) {
    return response.getResult().equals(1) || response.getResult().equals(true);
  }

}
