package org.interledger.ilp.ledger.adaptor.rest.json;

import org.interledger.ilp.core.ledger.model.LedgerMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonLedgerMessage implements LedgerMessage{

  private String from;
  private String to;
  private Object data;
  private String ledger;

  @JsonProperty(value="from")
  @Override
  public String getFromAccount() {
    return this.from;
  }

  @JsonProperty(value="to")
  @Override
  public String getToAccount() {
    return this.to;
  }

  @Override
  public Object getData() {
    return this.data;
  }

  @Override
  public String getLedger() {
    return this.ledger;
  }

  public void setFromAccount(String from) {
    this.from = from;
  }

  public void setToAccount(String to) {
    this.to = to;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public void setLedger(String ledger) {
    this.ledger = ledger;
  }

}
