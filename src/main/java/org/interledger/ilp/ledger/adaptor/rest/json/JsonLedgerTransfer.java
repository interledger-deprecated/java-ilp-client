package org.interledger.ilp.ledger.adaptor.rest.json;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.LedgerTransferAccountEntry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class JsonLedgerTransfer implements LedgerTransfer {

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
  @JsonSerialize(contentAs = JsonLedgerTransferAccountEntry.class)
  @Override
  public List<LedgerTransferAccountEntry> getCredits() {
    return credits;
  }

  @JsonDeserialize(contentAs = JsonLedgerTransferAccountEntry.class)
  public void setCredits(List<LedgerTransferAccountEntry> credits) {
    this.credits = credits;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getDebits()
   */
  @JsonSerialize(contentAs = JsonLedgerTransferAccountEntry.class)
  @Override
  public List<LedgerTransferAccountEntry> getDebits() {
    return debits;
  }

  @JsonDeserialize(contentAs = JsonLedgerTransferAccountEntry.class)
  public void setDebits(List<LedgerTransferAccountEntry> debits) {
    this.debits = debits;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getExecutionCondition()
   */
  @Override
  @JsonProperty("execution_condition")
  public String getExecutionCondition() {
    return executionCondition;
  }

  @JsonProperty("execution_condition")
  public void setExecutionCondition(String executionCondition) {
    this.executionCondition = executionCondition;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getCancellationCondition()
   */
  @Override
  @JsonProperty("cancellation_condition")
  public String getCancellationCondition() {
    return cancellationCondition;
  }

  @JsonProperty("cancellation_condition")
  public void setCancellationCondition(String cancellationCondition) {
    this.cancellationCondition = cancellationCondition;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerTransfer#getExpiresAt()
   */
  @Override
  @JsonProperty("expires_at")
  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  @JsonProperty("expires_at")
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

  public void setLedger(URI ledger) {
    this.ledgerId = ledger;
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
