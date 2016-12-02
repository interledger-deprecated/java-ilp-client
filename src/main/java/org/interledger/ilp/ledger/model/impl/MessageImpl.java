package org.interledger.ilp.ledger.model.impl;

import org.interledger.ilp.core.ledger.model.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageImpl implements Message {

  private String from;
  private String to;
  private Object data;
  private String ledger;
  
  @JsonCreator
  public MessageImpl() {
    
  }
  
  public MessageImpl(String from, String to, Object data) {
    this.from = from;
    this.to = to;
    this.data = data;
  }

  @Override
  @JsonProperty("from")
  public String getFromAccount() {
    return from;
  }

  @Override
  @JsonProperty("to")
  public String getToAccount() {
    return to;
  }

  @Override
  @JsonProperty("data")
  public Object getData() {
    return data;
  }
  
  @JsonProperty("ledger")
  public String getLedger() {
    return ledger;
  }
  
  public void setFrom(String from) {
    this.from = from;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public void setData(Object data) {
    this.data = data;
  }
  
  public void setLedger(String ledger) {
    this.ledger = ledger;
  }
}

