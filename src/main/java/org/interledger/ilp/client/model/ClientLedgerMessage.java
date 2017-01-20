package org.interledger.ilp.client.model;

import java.util.UUID;

import org.interledger.ilp.core.InterledgerAddress;
import org.interledger.ilp.core.ledger.model.LedgerMessage;

public class ClientLedgerMessage implements LedgerMessage {

  private UUID id;
  private String type;
  private InterledgerAddress from;
  private InterledgerAddress to;
  private Object data;
  
  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getType() {
    return type;
  }
  
  @Override
  public InterledgerAddress getFrom() {
    return this.from;
  }

  @Override
  public InterledgerAddress getTo() {
    return this.to;
  }

  @Override
  public Object getData() {
    return this.data;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setFrom(InterledgerAddress from) {
    this.from = from;
  }

  public void setTo(InterledgerAddress to) {
    this.to = to;
  }

  public void setData(Object data) {
    this.data = data;
  }


}
