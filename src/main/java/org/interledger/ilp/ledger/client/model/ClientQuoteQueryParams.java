package org.interledger.ilp.ledger.client.model;

import java.util.Set;

public class ClientQuoteQueryParams {
  
  private String sourceAddress;
  private String sourceAmount;
  private String destinationAmount;
  private String destinationAddress;
  private int destinationExpiryDuration;
  private String destinationPrecision;
  private String destinationScale;
  private Set<String> connectors;
  
  public String getSourceAddress() {
    return sourceAddress;
  }
  public String getSourceAmount() {
    return sourceAmount;
  }
  public String getDestinationAmount() {
    return destinationAmount;
  }
  public String getDestinationAddress() {
    return destinationAddress;
  }
  public int getDestinationExpiryDuration() {
    return destinationExpiryDuration;
  }
  public String getDestinationPrecision() {
    return destinationPrecision;
  }
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
