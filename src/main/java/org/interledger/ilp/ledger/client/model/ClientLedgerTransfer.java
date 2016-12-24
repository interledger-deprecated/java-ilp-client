package org.interledger.ilp.ledger.client.model;

import java.time.ZonedDateTime;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientLedgerTransfer implements LedgerTransfer {

  private String id;
  private String ledger;
  private String fromAccount;
  private String toAccount;
  private String amount;
  private boolean authorized;
  private String invoice;
  private byte[] data;
  private String executionCondition;
  private String cancellationCondition;
  private ZonedDateTime expiresAt;
  private boolean rejected;
  private String rejectionMessage;

  public String getId() {
    return id;
  }

  public String getLedger() {
    return ledger;
  }

  public String getFromAccount() {
    return fromAccount;
  }

  public String getToAccount() {
    return toAccount;
  }

  public String getAmount() {
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

  public String getExecutionCondition() {
    return executionCondition;
  }

  public String getCancellationCondition() {
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

  public void setId(String id) {
    this.id = id;
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

  public void setAmount(String amount) {
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

  public void setExecutionCondition(String executionCondition) {
    this.executionCondition = executionCondition;
  }

  public void setCancellationCondition(String cancellationCondition) {
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

  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe);
    }
  }
}
