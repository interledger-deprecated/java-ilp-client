package org.interledger.ilp.ledger.adaptor.rest.json;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class JsonLedgerTransferAccountEntry {

  private URI account;
  private String amount;
  private boolean authorized;
  private URI invoice;
  private Object memo;
  private boolean rejected;
  private String rejectionMessage;

  public URI getAccount() {
    return account;
  }

  public void setAccount(URI account) {
    this.account = account;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void setAuthorized(boolean authorized) {
    this.authorized = Boolean.valueOf(authorized);
  }

  public URI getInvoice() {
    return invoice;
  }

  public void setInvoice(URI invoice) {
    this.invoice = invoice;
  }

  public Object getMemo() {
    return memo;
  }

  public void setMemo(Object memo) {
    this.memo = memo;
  }

  public boolean isRejected() {
    return rejected;
  }

  public void setRejected(boolean rejected) {
    this.rejected = Boolean.valueOf(rejected);
  }

  @JsonProperty("rejection_message")
  public String getRejectionMessage() {
    return rejectionMessage;
  }

  @JsonProperty("rejection_message")
  public void setRejectionMessage(String rejectionMessage) {
    this.rejectionMessage = rejectionMessage;
  }
}
