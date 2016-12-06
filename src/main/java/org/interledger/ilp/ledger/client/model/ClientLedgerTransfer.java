package org.interledger.ilp.ledger.client.model;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.LedgerTransferAccountEntry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientLedgerTransfer implements LedgerTransfer {

  private List<LedgerTransferAccountEntry> credits;
  private List<LedgerTransferAccountEntry> debits;
  private String executionCondition;
  private String cancellationCondition;
  private ZonedDateTime expiresAt;
  private String id;
  private URI ledgerId;

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getCredits()
   */
  @Override
  public List<LedgerTransferAccountEntry> getCredits() {
    return credits;
  }

  public void setCredits(List<LedgerTransferAccountEntry> credits) {
    this.credits = credits;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getDebits()
   */
  @Override
  public List<LedgerTransferAccountEntry> getDebits() {
    return debits;
  }

  public void setDebits(List<LedgerTransferAccountEntry> debits) {
    this.debits = debits;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getExecutionCondition()
   */
  @Override
  public String getExecutionCondition() {
    return executionCondition;
  }

  public void setExecutionCondition(String executionCondition) {
    this.executionCondition = executionCondition;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getCancellationCondition()
   */
  @Override
  public String getCancellationCondition() {
    return cancellationCondition;
  }

  public void setCancellationCondition(String cancellationCondition) {
    this.cancellationCondition = cancellationCondition;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getExpiresAt()
   */
  @Override
  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getLedger()
   */
  @Override
  public URI getLedger() {
    return ledgerId;
  }

  public void setLedger(URI ledgerId) {
    this.ledgerId = ledgerId;
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
