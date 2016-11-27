package org.interledger.ilp.ledger.model.impl;

import org.interledger.ilp.core.InterledgerPacketHeader;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;

public class Transfer implements LedgerTransfer {

  private InterledgerPacketHeader header;
  private String id;
  private String fromAccount;
  private String toAccount;
  private String amount;
  private String data;
  private String noteToSelf;

  @Override
  public InterledgerPacketHeader getHeader() {
    return header;
  }
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getFromAccount() {
    return fromAccount;
  }

  @Override
  public String getToAccount() {
    return toAccount;
  }

  @Override
  public String getAmount() {
    return amount;
  }

  @Override
  public String getData() {
    return data;
  }

  @Override
  public String getNoteToSelf() {
    return noteToSelf;
  }

  public void setHeader(InterledgerPacketHeader header) {
    this.header = header;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  public void setFromAccount(String fromAccount) {
    this.fromAccount = fromAccount;
  }

  public void setToAccount(String toAccount) {
    this.toAccount = toAccount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public void setData(String data) {
    this.data = data;
  }

  public void setNoteToSelf(String noteToSelf) {
    this.noteToSelf = noteToSelf;
  }

}
