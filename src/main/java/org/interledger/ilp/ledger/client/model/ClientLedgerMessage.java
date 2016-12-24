package org.interledger.ilp.ledger.client.model;

import org.interledger.ilp.core.ledger.model.LedgerMessage;

public class ClientLedgerMessage implements LedgerMessage {

  private String ledger;
  private String from;
  private String to;
  private byte[] data;

  
  @Override
  public String getLedger() {
    return this.ledger;
  }
  
  @Override
  public String getFrom() {
    return this.from;
  }

  @Override
  public String getTo() {
    return this.to;
  }

  @Override
  public byte[] getData() {
    return this.data;
  }
  
  public void setLedger(String ledger) {
    this.ledger = ledger;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
