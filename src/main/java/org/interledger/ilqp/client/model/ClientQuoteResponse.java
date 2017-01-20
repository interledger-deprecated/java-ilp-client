package org.interledger.ilqp.client.model;

import java.time.Duration;

import javax.money.MonetaryAmount;

import org.interledger.ilp.core.InterledgerAddress;
import org.interledger.ilqp.core.model.QuoteResponse;

public class ClientQuoteResponse implements QuoteResponse {

  private InterledgerAddress sourceLedger;
  private InterledgerAddress destinationLedger;
  private InterledgerAddress sourceConnectorAccount;
  private MonetaryAmount sourceAmount;
  private MonetaryAmount destinationAmount;
  private Duration sourceExpiryDuration;
  private Duration destinationExpiryDuration;
  
  @Override
  public InterledgerAddress getSourceLedger() {
    return sourceLedger;
  }

  @Override
  public InterledgerAddress getDestinationLedger() {
    return destinationLedger;
  }

  @Override
  public InterledgerAddress getSourceConnectorAccount() {
    return sourceConnectorAccount;
  }

  @Override
  public MonetaryAmount getSourceAmount() {
    return sourceAmount;
  }

  @Override
  public MonetaryAmount getDestinationAmount() {
    return destinationAmount;
  }

  @Override
  public Duration getSourceExpiryDuration() {
    return sourceExpiryDuration;
  }

  @Override
  public Duration getDestinationExpiryDuration() {
    return destinationExpiryDuration;
  }

  public void setSourceLedger(InterledgerAddress sourceLedger) {
    this.sourceLedger = sourceLedger;
  }

  public void setDestinationLedger(InterledgerAddress destinationLedger) {
    this.destinationLedger = destinationLedger;
  }

  public void setSourceConnectorAccount(InterledgerAddress sourceAccount) {
    this.sourceConnectorAccount = sourceAccount;
  }

  public void setSourceAmount(MonetaryAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  public void setDestinationAmount(MonetaryAmount destinationAmount) {
    this.destinationAmount = destinationAmount;
  }

  public void setSourceExpiryDuration(Duration sourceExpiryDuration) {
    this.sourceExpiryDuration = sourceExpiryDuration;
  }

  public void setDestinationExpiryDuration(Duration destinationExpiryDuration) {
    this.destinationExpiryDuration = destinationExpiryDuration;
  }

}
