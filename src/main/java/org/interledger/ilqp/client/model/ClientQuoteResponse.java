package org.interledger.ilqp.client.model;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.quoting.model.QuoteResponse;

import java.time.Duration;

import javax.money.MonetaryAmount;

/**
 * Concrete implementation of an ILQP quote response.a
 */
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
