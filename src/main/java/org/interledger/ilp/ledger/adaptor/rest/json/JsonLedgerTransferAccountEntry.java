package org.interledger.ilp.ledger.adaptor.rest.json;

import java.net.URI;

import org.interledger.ilp.core.ledger.model.LedgerTransferAccountEntry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class JsonLedgerTransferAccountEntry implements LedgerTransferAccountEntry {

  private URI account;
  private String amount;
  private Boolean authorized;
  private URI invoice;
  private Object memo;
  private Boolean rejected;
  private String rejectionMessage;

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#getAccount()
   */
  @Override
  public URI getAccount() {
    return account;
  }

  public void setAccount(URI account) {
    this.account = account;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#getAmount()
   */
  @Override
  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#isAuthorized()
   */
  @Override
  public Boolean isAuthorized() {
    return authorized;
  }

  public void setAuthorized(boolean authorized) {
    this.authorized = Boolean.valueOf(authorized);
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#getInvoice()
   */
  @Override
  public URI getInvoice() {
    return invoice;
  }

  public void setInvoice(URI invoice) {
    this.invoice = invoice;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#getMemo()
   */
  @Override
  public Object getMemo() {
    return memo;
  }

  public void setMemo(Object memo) {
    this.memo = memo;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#isRejected()
   */
  @Override
  public Boolean isRejected() {
    return rejected;
  }

  public void setRejected(boolean rejected) {
    this.rejected = Boolean.valueOf(rejected);
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransferAccountEntry#getRejectionMessage()
   */
  @Override
  @JsonProperty("rejection_message")
  public String getRejectionMessage() {
    return rejectionMessage;
  }

  @JsonProperty("rejection_message")
  public void setRejectionMessage(String rejectionMessage) {
    this.rejectionMessage = rejectionMessage;
  }
}
