package org.interledger.ilp.ledger.adaptor.rest.json;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonLedgerUrls {

  private URI accountsUrl;
  private URI accountUrl;
  private URI authTokenUrl;
  private URI healthUrl;
  private URI messageUrl;
  private URI transferFulfillmentUrl;
  private URI transferRejectionUrl;
  private URI transferStateUrl;
  private URI transferUrl;

  private URI websocketUrl;

  @JsonProperty(value = "accounts")
  public URI getAccountsUrl() {
    return accountsUrl;
  }

  @JsonProperty(value = "account")
  public URI getAccountUrl() {
    return accountUrl;
  }

  @JsonProperty(value = "auth_token")
  public URI getAuthTokenUrl() {
    return authTokenUrl;
  }

  @JsonProperty(value = "health")
  public URI getHealthUrl() {
    return healthUrl;
  }

  @JsonProperty(value = "message")
  public URI getMessageUrl() {
    return messageUrl;
  }

  @JsonProperty(value = "transfer_fulfillment")
  public URI getTransferFulfillmentUrl() {
    return transferFulfillmentUrl;
  }

  @JsonProperty(value = "transfer_rejection")
  public URI getTransferRejectionUrl() {
    return transferRejectionUrl;
  }

  @JsonProperty(value = "transfer_state")
  public URI getTransferStateUrl() {
    return transferStateUrl;
  }

  @JsonProperty(value = "transfer")
  public URI getTransferUrl() {
    return transferUrl;
  }

  @JsonProperty(value = "websocket")
  public URI getWebsocketUrl() {
    return websocketUrl;
  }

  public void setAuthTokenUrl(URI authTokenUrl) {
    this.authTokenUrl = authTokenUrl;
  }

  public void setAccountsUrl(URI accountsUrl) {
    this.accountsUrl = accountsUrl;
  }

  public void setAccountUrl(URI accountUrl) {
    this.accountUrl = accountUrl;
  }

  public void setHealthUrl(URI transferUrl) {
    this.healthUrl = transferUrl;
  }

  public void setMessageUrl(URI messageUrl) {
    this.messageUrl = messageUrl;
  }

  public void setTransferFulfillmentUrl(URI transferFulfillmentUrl) {
    this.transferFulfillmentUrl = transferFulfillmentUrl;
  }

  public void setTransferRejectionUrl(URI transferRejectionUrl) {
    this.transferRejectionUrl = transferRejectionUrl;
  }

  public void setTransferStateUrl(URI transferStateUrl) {
    this.transferStateUrl = transferStateUrl;
  }

  public void setTransferUrl(URI transferUrl) {
    this.transferUrl = transferUrl;
  }

  public void setWebsocketUrl(URI websocket) {
    this.websocketUrl = websocket;
  }
}
