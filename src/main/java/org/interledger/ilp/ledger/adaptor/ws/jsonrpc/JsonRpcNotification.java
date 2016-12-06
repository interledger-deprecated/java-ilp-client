package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=JsonRpcNotification.class)
public class JsonRpcNotification extends JsonRpcRequest {
  
  private JsonRpcNotificationParams params;

  @JsonProperty(value = "params")
  public JsonRpcNotificationParams getParams() {
    return this.params;
  }

  public void setParams(JsonRpcNotificationParams params) {
    this.params = params;
  }
  
}
