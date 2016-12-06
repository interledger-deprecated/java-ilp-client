package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=JsonRpcResponse.class)
public class JsonRpcResponse extends JsonRpcMessage {

  private Object result;
  private JsonRpcError error;

  @JsonProperty(value = "result")
  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  @JsonProperty(value = "error")
  public JsonRpcError getError() {
    return error;
  }

  public void setError(JsonRpcError error) {
    this.error = error;
  }

}
