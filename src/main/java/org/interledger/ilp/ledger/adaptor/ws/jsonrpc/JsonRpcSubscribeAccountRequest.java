package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=JsonRpcSubscribeAccountRequest.class)
public class JsonRpcSubscribeAccountRequest extends JsonRpcRequest {

  private JsonRpcSubscribeAccountRequestParams params;
  
  public JsonRpcSubscribeAccountRequest() {
    setMethod("subscribe_account");
  }
  

  @JsonProperty(value = "params")
  public JsonRpcSubscribeAccountRequestParams getParams() {
    return this.params;
  }

  public void setParams(JsonRpcSubscribeAccountRequestParams params) {
    this.params = params;
  }
}
