package org.interledger.ilp.ledger.client.model;

import org.interledger.ilp.core.InterledgerAddress;
import org.interledger.ilp.core.ledger.model.LedgerMessage;

public class ClientLedgerMessage implements LedgerMessage {

  private InterledgerAddress from;
  private InterledgerAddress to;
  private byte[] data;
  
  @Override
  public InterledgerAddress getFrom() {
    return this.from;
  }

  @Override
  public InterledgerAddress getTo() {
    return this.to;
  }

  @Override
  public byte[] getData() {
    return this.data;
  }

  public void setFrom(InterledgerAddress from) {
    this.from = from;
  }

  public void setTo(InterledgerAddress to) {
    this.to = to;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

}
