package org.interledger.ilp.client.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.money.MonetaryAmount;

import org.interledger.cryptoconditions.Condition;
import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.ledger.model.LedgerTransfer;

public class ClientLedgerTransfer implements LedgerTransfer {

  private UUID id;
  private InterledgerAddress fromAccount;
  private InterledgerAddress toAccount;
  private MonetaryAmount amount;
  private boolean authorized;
  private String invoice;
  private byte[] data;
  private byte[] noteToSelf;
  private Condition executionCondition;
  private Condition cancellationCondition;
  private ZonedDateTime expiresAt;
  private boolean rejected;
  private String rejectionMessage;

  public UUID getId() {
    return id;
  }

  public InterledgerAddress getFromAccount() {
    return fromAccount;
  }

  public InterledgerAddress getToAccount() {
    return toAccount;
  }

  public MonetaryAmount getAmount() {
    return amount;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public String getInvoice() {
    return invoice;
  }

  public byte[] getData() {
    return data;
  }

  public byte[] getNoteToSelf() {
    return noteToSelf;
  }

  public Condition getExecutionCondition() {
    return executionCondition;
  }

  public Condition getCancellationCondition() {
    return cancellationCondition;
  }

  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  public boolean isRejected() {
    return rejected;
  }

  public String getRejectionMessage() {
    return rejectionMessage;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setFromAccount(InterledgerAddress fromAccount) {
    this.fromAccount = fromAccount;
  }

  public void setToAccount(InterledgerAddress toAccount) {
    this.toAccount = toAccount;
  }

  public void setAmount(MonetaryAmount amount) {
    this.amount = amount;
  }

  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }

  public void setInvoice(String invoice) {
    this.invoice = invoice;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public void setNoteToSelf(byte[] noteToSelf) {
    this.noteToSelf = noteToSelf;
  }

  public void setExecutionCondition(Condition executionCondition) {
    this.executionCondition = executionCondition;
  }

  public void setCancellationCondition(Condition cancellationCondition) {
    this.cancellationCondition = cancellationCondition;
  }

  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public void setRejected(boolean rejected) {
    this.rejected = rejected;
  }

  public void setRejectionMessage(String rejectionMessage) {
    this.rejectionMessage = rejectionMessage;
  }

}
