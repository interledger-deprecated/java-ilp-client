package org.interledger.ilp.ledger.client.json;

import java.math.BigDecimal;

import org.interledger.ilp.core.client.model.MessageData;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class JsonQuoteResponse implements MessageData, Comparable<JsonQuoteResponse>{
  
  private String sourceLedger;
  private String destinationLedger;
  private String sourceConnectorAccount;
  private String sourceAmount;
  private String destinationAmount;
  private int sourceExpiryDuration;
  private int destinationExpiryDuration;
  
  @JsonProperty("source_ledger")
  public String getSourceLedger() {
    return sourceLedger;
  }
  
  @JsonProperty("destination_ledger")
  public String getDestinationLedger() {
    return destinationLedger;
  }
  
  @JsonProperty("source_connector_account")
  public String getSourceConnectorAccount() {
    return sourceConnectorAccount;
  }
  
  @JsonProperty("source_amount")
  public String getSourceAmount() {
    return sourceAmount;
  }
  
  @JsonProperty("destination_amount")
  public String getDestinationAmount() {
    return destinationAmount;
  }
  
  @JsonProperty("source_expiry_duration")
  public int getSourceExpiryDuration() {
    return sourceExpiryDuration;
  }
  
  @JsonProperty("destination_expiry_duration")
  public int getDestinationExpiryDuration() {
    return destinationExpiryDuration;
  }

  public void setSourceLedger(String sourceLedger) {
    this.sourceLedger = sourceLedger;
  }

  public void setDestinationLedger(String destinationLedger) {
    this.destinationLedger = destinationLedger;
  }

  public void setSourceConnectorAccount(String sourceConnectorAccount) {
    this.sourceConnectorAccount = sourceConnectorAccount;
  }

  public void setSourceAmount(String sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  public void setDestinationAmount(String destinationAmount) {
    this.destinationAmount = destinationAmount;
  }

  public void setSourceExpiryDuration(int sourceExpiryDuration) {
    this.sourceExpiryDuration = sourceExpiryDuration;
  }

  public void setDestinationExpiryDuration(int destinationExpiryDuration) {
    this.destinationExpiryDuration = destinationExpiryDuration;
  }

  @Override
  public int compareTo(JsonQuoteResponse other) {
    
    //Need to check later if this was set
    
    final int UNKNOWN = -1; //Any non-zero value would work
    int compareSource = UNKNOWN;
    
    if(getSourceAmount() != null) {
      if(other.getSourceAmount() != null) {
        BigDecimal sourceAmount = new BigDecimal(getSourceAmount());
        BigDecimal otherSourceAmount = new BigDecimal(other.getSourceAmount());
        compareSource = sourceAmount.compareTo(otherSourceAmount); 
        if(compareSource != 0) {
          return compareSource;
        }
      }
    }
    
    if(getDestinationAmount() != null) {
      if(other.getDestinationAmount() != null) {
        BigDecimal destinationAmount = new BigDecimal(getDestinationAmount());
        BigDecimal otherDestinationAmount = new BigDecimal(other.getDestinationAmount());
        compareSource = otherDestinationAmount.compareTo(destinationAmount); 
        if(compareSource != 0) {
          return compareSource;
        }
      }
    }
    
    if(compareSource == UNKNOWN) {
      throw new IllegalArgumentException("Quotes must either both have a source amount or both have a destination amount.");
    }
    
    return compareSource;
 }
  
  


}
