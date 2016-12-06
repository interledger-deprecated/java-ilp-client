package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class JsonRpcRequest extends JsonRpcMessage {
  
  private String method;
  
  @JsonProperty(value = "method")
  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

}



