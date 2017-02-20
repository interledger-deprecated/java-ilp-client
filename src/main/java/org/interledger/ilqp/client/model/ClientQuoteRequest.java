package org.interledger.ilqp.client.model;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.quoting.model.QuoteRequest;

import java.time.Duration;

import javax.money.MonetaryAmount;

/**
 * Concrete implementation of an ILQP quote request.
 */
public class ClientQuoteRequest implements QuoteRequest {

  private InterledgerAddress sourceAddress;
  private MonetaryAmount sourceAmount;
  private Duration sourceExpiryDuration;
  private InterledgerAddress destinationAddress;
  private MonetaryAmount destinationAmount;
  private Duration destinationExpiryDuration;

  @Override
  public InterledgerAddress getSourceAddress() {
    return sourceAddress;
  }

  @Override
  public MonetaryAmount getSourceAmount() {
    return sourceAmount;
  }

  @Override
  public Duration getSourceExpiryDuration() {
    return sourceExpiryDuration;
  }

  @Override
  public InterledgerAddress getDestinationAddress() {
    return destinationAddress;
  }

  @Override
  public MonetaryAmount getDestinationAmount() {
    return destinationAmount;
  }

  @Override
  public Duration getDestinationExpiryDuration() {
    return destinationExpiryDuration;
  }

  public void setSourceAddress(InterledgerAddress sourceAddress) {
    this.sourceAddress = sourceAddress;
  }

  public void setSourceAmount(MonetaryAmount sourceAmount) {
    this.sourceAmount = sourceAmount;
  }

  public void setSourceExpiryDuration(Duration sourceExpiryDuration) {
    this.sourceExpiryDuration = sourceExpiryDuration;
  }

  public void setDestinationAddress(InterledgerAddress destinationAddress) {
    this.destinationAddress = destinationAddress;
  }

  public void setDestinationAmount(MonetaryAmount destinationAmount) {
    this.destinationAmount = destinationAmount;
  }

  public void setDestinationExpiryDuration(Duration destinationExpiryDuration) {
    this.destinationExpiryDuration = destinationExpiryDuration;
  }

}
