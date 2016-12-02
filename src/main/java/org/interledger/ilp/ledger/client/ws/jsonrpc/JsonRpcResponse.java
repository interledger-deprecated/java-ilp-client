package org.interledger.ilp.ledger.client.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcResponse extends JsonRpcBase {

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
