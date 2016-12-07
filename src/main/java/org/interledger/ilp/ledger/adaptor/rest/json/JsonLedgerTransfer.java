package org.interledger.ilp.ledger.adaptor.rest.json;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.List;

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
public class JsonLedgerTransfer {

  private List<JsonLedgerTransferAccountEntry> credits;
  private List<JsonLedgerTransferAccountEntry> debits;
  private String executionCondition;
  private String cancellationCondition;
  private ZonedDateTime expiresAt;
  private URI id;
  private URI ledgerId;

  @JsonSerialize(contentAs = JsonLedgerTransferAccountEntry.class)
  public List<JsonLedgerTransferAccountEntry> getCredits() {
    return credits;
  }

  @JsonDeserialize(contentAs = JsonLedgerTransferAccountEntry.class)
  public void setCredits(List<JsonLedgerTransferAccountEntry> credits) {
    this.credits = credits;
  }

  @JsonSerialize(contentAs = JsonLedgerTransferAccountEntry.class)
  public List<JsonLedgerTransferAccountEntry> getDebits() {
    return debits;
  }

  @JsonDeserialize(contentAs = JsonLedgerTransferAccountEntry.class)
  public void setDebits(List<JsonLedgerTransferAccountEntry> debits) {
    this.debits = debits;
  }

  @JsonProperty("execution_condition")
  public String getExecutionCondition() {
    return executionCondition;
  }

  @JsonProperty("execution_condition")
  public void setExecutionCondition(String executionCondition) {
    this.executionCondition = executionCondition;
  }
  @JsonProperty("cancellation_condition")
  public String getCancellationCondition() {
    return cancellationCondition;
  }

  @JsonProperty("cancellation_condition")
  public void setCancellationCondition(String cancellationCondition) {
    this.cancellationCondition = cancellationCondition;
  }

  @JsonProperty("expires_at")
  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  @JsonProperty("expires_at")
  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @JsonProperty("id")
  public URI getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(URI id) {
    this.id = id;
  }

  @JsonProperty("ledger")
  public URI getLedgerId() {
    return ledgerId;
  }

  @JsonProperty("ledger")
  public void setLedgerId(URI ledger) {
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
