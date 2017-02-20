package org.interledger.ilp.client.model;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.ledger.model.AccountInfo;

import java.security.PublicKey;

import javax.money.MonetaryAmount;

/**
 * Concrete implementation defining information about a given account on the ledger.
 */
public class ClientAccountInfo implements AccountInfo {

  private String id;
  private InterledgerAddress ledger;
  private String name;
  private InterledgerAddress address;
  private MonetaryAmount balance;
  private boolean isDisabled;
  private byte[] certificateFingerprint;
  private MonetaryAmount minimumAllowedBalance;
  private PublicKey publicKey;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public InterledgerAddress getLedger() {
    return ledger;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public InterledgerAddress getAddress() {
    return address;
  }

  @Override
  public MonetaryAmount getBalance() {
    return balance;
  }

  @Override
  public boolean isDisabled() {
    return isDisabled;
  }

  @Override
  public byte[] getCertificateFingerprint() {
    return certificateFingerprint;
  }

  @Override
  public MonetaryAmount getMinimumAllowedBalance() {
    return minimumAllowedBalance;
  }

  @Override
  public PublicKey getPublicKey() {
    return publicKey;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setLedger(InterledgerAddress ledger) {
    this.ledger = ledger;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAddress(InterledgerAddress address) {
    this.address = address;
  }

  public void setBalance(MonetaryAmount balance) {
    this.balance = balance;
  }

  public void setIsDisabled(boolean isDisabled) {
    this.isDisabled = isDisabled;
  }

  public void setCertificateFingerprint(byte[] certificateFingerprint) {
    this.certificateFingerprint = certificateFingerprint;
  }

  public void setMinimumAllowedBalance(MonetaryAmount minimumAllowedBalance) {
    this.minimumAllowedBalance = minimumAllowedBalance;
  }

  public void setPublicKey(PublicKey publicKey) {
    this.publicKey = publicKey;
  }

}
