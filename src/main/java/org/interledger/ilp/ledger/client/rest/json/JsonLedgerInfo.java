package org.interledger.ilp.ledger.client.rest.json;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.interledger.ilp.core.ledger.model.ConnectorInfo;
import org.interledger.ilp.core.ledger.model.LedgerInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonLedgerInfo implements LedgerInfo {

  private int precision;
  private int scale;
  private String currencyCode;
  private String currencySymbol;
  private Map<String, URI> urls;
  private List<ConnectorInfo> connectors;
  private String notificationSignPublicKey;
  private String conditionSignPublicKey;
  private String ledgerPrefix;

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.client.rest.json.LedgerInfo#getPrecision()
   */
  @Override
  @JsonProperty(value = "precision")
  public int getPrecision() {
    return precision;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.client.rest.json.LedgerInfo#getScale()
   */
  @Override
  @JsonProperty(value = "scale")
  public int getScale() {
    return scale;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.client.rest.json.LedgerInfo#getCurrencyCode()
   */
  @Override
  @JsonProperty(value = "currency_code")
  public String getCurrencyCode() {
    return currencyCode;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.client.rest.json.LedgerInfo#getCurrencySymbol()
   */
  @Override
  @JsonProperty(value = "currency_symbol")
  public String getCurrencySymbol() {
    return currencySymbol;
  }

  @Override
  @JsonProperty(value = "ilp_prefix")
  public String getLedgerPrefix() {
    return this.ledgerPrefix;
  }

  @Override
  @JsonProperty(value = "condition_sign_public_key")
  public String getConditionSignPublicKey() {
    return this.conditionSignPublicKey;
  }

  @Override
  @JsonProperty(value = "notification_sign_public_key")
  public String getNotificationSignPublicKey() {
    return this.notificationSignPublicKey;
  }
  
  @JsonProperty(value = "urls")
  public Map<String, URI> getUrls() {
    return urls;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.client.rest.json.LedgerInfo#getConnectors()
   */
  @Override
  @JsonProperty(value = "connectors")
  @JsonDeserialize(contentAs = JsonConnectorInfo.class)
  @JsonSerialize(contentAs = JsonConnectorInfo.class)
  public List<ConnectorInfo> getConnectors() {
    return connectors;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public void setUrls(Map<String, URI> urls) {
    this.urls = urls;
  }

  public void setConnectors(List<ConnectorInfo> connectors) {
    this.connectors = connectors;
  }
  
  public void setLedgerPrefix(String ledgerPrefix) {
    this.ledgerPrefix = ledgerPrefix;
  }

  public void setConditionSignPublicKey(String key) {
    this.conditionSignPublicKey = key;
  }

  public void setNotificationSignPublicKey(String key) {
    this.notificationSignPublicKey = key;
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
