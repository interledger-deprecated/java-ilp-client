package org.interledger.ilp.ledger.client.rest.json;

import java.time.ZonedDateTime;
import java.util.List;

import org.interledger.ilp.core.InterledgerPacketHeader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class JsonLedgerTransfer {

  private List<JsonTransferAccountEntry> credits;
  private List<JsonTransferAccountEntry> debits;
  private String executionCondition;
  private String cancellationCondition;
  private ZonedDateTime expiresAt;
  private String id;
  private String ledger;

  public List<JsonTransferAccountEntry> getCredits() {
    return credits;
  }

  public void setCredits(List<JsonTransferAccountEntry> credits) {
    this.credits = credits;
  }

  public List<JsonTransferAccountEntry> getDebits() {
    return debits;
  }

  public void setDebits(List<JsonTransferAccountEntry> debits) {
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLedger() {
    return ledger;
  }

  public void setLedger(String ledger) {
    this.ledger = ledger;
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
