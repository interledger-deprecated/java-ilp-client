package org.interledger.ilp.ledger.client.model;

public class ClientQuoteResponse {
  
  private String sourceAddress;
  private String sourceAmount;
  private String destinationAmount;
  private String destinationAddress;
  private int destinationExpiryDuration;
  
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

}
