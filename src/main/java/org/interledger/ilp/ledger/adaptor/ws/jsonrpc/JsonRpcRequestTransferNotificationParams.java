package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcRequestTransferNotificationParams extends JsonRpcNotificationParams {

  private JsonLedgerTransfer transfer;
  
  @JsonProperty(value = "resource")
  public JsonLedgerTransfer getTransfer() {
    return this.transfer;
  }
  
  public void setTransfer(JsonLedgerTransfer message) {
    this.transfer = message;
  }
}
