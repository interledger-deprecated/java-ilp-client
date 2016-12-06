package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcRequestMessageNotificationParams extends JsonRpcNotificationParams {
  
  private JsonLedgerMessage message;
  
  @JsonProperty(value = "resource")
  public JsonLedgerMessage getMessage() {
    return this.message;
  }
  
  public void setMessage(JsonLedgerMessage message) {
    this.message = message;
  }

}
