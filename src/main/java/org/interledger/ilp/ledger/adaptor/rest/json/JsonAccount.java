package org.interledger.ilp.ledger.adaptor.rest.json;

import org.interledger.ilp.core.ledger.model.Account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonAccount implements Account {

  private String balance;
  private String certificateFingerprint;
  private String id;
  private boolean isAdmin;
  private boolean isDisabled;
  private String ledger;
  private String minimumAllowedBalance;
  private String name;
  private String password;
  private String publicKey;

  @Override
  @JsonProperty(value = "balance")
  public String getBalance() {
    return balance;
  }

  @Override
  @JsonProperty(value = "fingerprint")
  public String getCertificateFingerprint() {
    return certificateFingerprint;
  }

  @Override
  @JsonProperty(value = "id")
  public String getId() {
    return id;
  }

  @Override
  @JsonProperty(value = "ledger")
  public String getLedger() {
    return ledger;
  }

  @Override
  @JsonProperty(value = "minimum_allowed_balance")
  public String getMinimumAllowedBalance() {
    return minimumAllowedBalance;
  }

  @Override
  @JsonProperty(value = "name")
  public String getName() {
    return name;
  }

  @Override
  @JsonProperty(value = "password")
  public String getPassword() {
    return password;
  }

  @Override
  @JsonProperty(value = "public_key")
  public String getPublicKey() {
    return publicKey;
  }

  @Override
  @JsonProperty(value = "is_admin")
  public boolean isAdmin() {
    return isAdmin;
  }

  @Override
  @JsonProperty(value = "is_disabled")
  public boolean isDisabled() {
    return isDisabled;
  }

  public void setAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public void setCertificateFingerprint(String certificateFingerprint) {
    this.certificateFingerprint = certificateFingerprint;
  }

  public void setDisabled(boolean isDisabled) {
    this.isDisabled = isDisabled;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setLedger(String ledger) {
    this.ledger = ledger;
  }

  public void setMinimumAllowedBalance(String minimumAllowedBalance) {
    this.minimumAllowedBalance = minimumAllowedBalance;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  @Override
  public String toString() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
