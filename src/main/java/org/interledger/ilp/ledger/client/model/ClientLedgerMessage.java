package org.interledger.ilp.ledger.client.model;

import org.interledger.ilp.core.ledger.model.LedgerMessage;

public class ClientLedgerMessage implements LedgerMessage {

  private String ledger;
  private String fromAccount;
  private String toAccount;
  private String data;

  @Override
  public String getLedger() {
    return this.ledger;
  }
  
  @Override
  public String getFromAccount() {
    return this.fromAccount;
  }

  @Override
  public String getToAccount() {
    return this.toAccount;
  }

  @Override
  public Object getData() {
    return this.data;
  }
  
  public void setLedger(String ledger) {
    this.ledger = ledger;
  }

  public void setFromAccount(String fromAccount) {
    this.fromAccount = fromAccount;
  }

  public void setToAccount(String toAccount) {
    this.toAccount = toAccount;
  }

  public void setData(String data) {
    this.data = data;
  }

}
