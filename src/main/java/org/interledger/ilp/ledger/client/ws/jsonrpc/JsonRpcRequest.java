package org.interledger.ilp.ledger.client.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcRequest extends JsonRpcBase {
  
  private String method;
  private Object params;
  
  @JsonProperty(value = "method")
  public String getMethod() {
    return method;
  }

  @JsonProperty(value = "params")
  public Object getParams() {
    return params;
  }
  
  public void setMethod(String method) {
    this.method = method;
  }

  public void setParams(Object params) {
    this.params = params;
  }
}

