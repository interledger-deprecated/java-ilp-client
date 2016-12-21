package org.interledger.ilp.ledger.client.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ClientQuoteQueryParams {
  
  private String sourceAddress;
  private String sourceAmount;
  private String destinationAmount;
  private String destinationAddress;
  private int destinationExpiryDuration;
  private String destinationPrecision;
  private String destinationScale;
  private Set<String> connectors;
  
  @JsonProperty("source_address")
  public String getSourceAddress() {
    return sourceAddress;
  }

  @JsonProperty("source_amount")
  public String getSourceAmount() {
    return sourceAmount;
  }

  @JsonProperty("destination_amount")
  public String getDestinationAmount() {
    return destinationAmount;
  }
  
  @JsonProperty("destination_address")
  public String getDestinationAddress() {
    return destinationAddress;
  }
  
  @JsonProperty("destination_expiry_duration")
  public int getDestinationExpiryDuration() {
    return destinationExpiryDuration;
  }
  
  //TODO: not in https://interledger.org/rfcs/0008-interledger-quoting-protocol/ ??
  @JsonProperty("destination_precision")
  public String getDestinationPrecision() {
    return destinationPrecision;
  }

  //TODO: not in https://interledger.org/rfcs/0008-interledger-quoting-protocol/ ??
  @JsonProperty("destination_scale")
  public String getDestinationScale() {
    return destinationScale;
  }
  
  public Set<String> getConnectors() {
    return connectors;
  }
  public void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }
  public void setSourceAmount(String sourceAmount) {
    this.sourceAmount = sourceAmount;
  }
  public void setDestinationAmount(String destinationAmount) {
    this.destinationAmount = destinationAmount;
  }
  public void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }
  public void setDestinationExpiryDuration(int destinationExpiryDuration) {
    this.destinationExpiryDuration = destinationExpiryDuration;
  }
  public void setDestinationPrecision(String destinationPrecision) {
    this.destinationPrecision = destinationPrecision;
  }
  public void setDestinationScale(String destinationScale) {
    this.destinationScale = destinationScale;
  }
  public void setConnectors(Set<String> connectors) {
    this.connectors = connectors;
  }
}
