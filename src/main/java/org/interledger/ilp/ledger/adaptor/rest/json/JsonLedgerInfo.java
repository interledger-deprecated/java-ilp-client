package org.interledger.ilp.ledger.adaptor.rest.json;

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

  private String conditionSignPublicKey;
  private List<ConnectorInfo> connectors;
  private String currencyCode;
  private String currencySymbol;
  private URI id;
  private String ledgerPrefix;
  private String notificationSignPublicKey;
  private int precision;
  private int scale;
  private Map<String, URI> urls;

  @Override
  @JsonProperty(value = "condition_sign_public_key")
  public String getConditionSignPublicKey() {
    return this.conditionSignPublicKey;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerInfo#getConnectors()
   */
  @Override
  @JsonProperty(value = "connectors")
  @JsonDeserialize(contentAs = JsonConnectorInfo.class)
  @JsonSerialize(contentAs = JsonConnectorInfo.class)
  public List<ConnectorInfo> getConnectors() {
    return connectors;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerInfo#getCurrencyCode()
   */
  @Override
  @JsonProperty(value = "currency_code")
  public String getCurrencyCode() {
    return currencyCode;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerInfo#getCurrencySymbol()
   */
  @Override
  @JsonProperty(value = "currency_symbol")
  public String getCurrencySymbol() {
    return currencySymbol;
  }

  @Override
  public URI getId() {
    return id;
  }

  @Override
  @JsonProperty(value = "ilp_prefix")
  public String getLedgerPrefix() {
    return this.ledgerPrefix;
  }

  @Override
  @JsonProperty(value = "notification_sign_public_key")
  public String getNotificationSignPublicKey() {
    return this.notificationSignPublicKey;
  }
  
  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerInfo#getPrecision()
   */
  @Override
  @JsonProperty(value = "precision")
  public int getPrecision() {
    return precision;
  }

  /* (non-Javadoc)
   * @see org.interledger.ilp.ledger.adaptor.rest.json.LedgerInfo#getScale()
   */
  @Override
  @JsonProperty(value = "scale")
  public int getScale() {
    return scale;
  }

  @JsonProperty(value = "urls")
  public Map<String, URI> getUrls() {
    return urls;
  }

  public void setConditionSignPublicKey(String key) {
    this.conditionSignPublicKey = key;
  }

  public void setConnectors(List<ConnectorInfo> connectors) {
    this.connectors = connectors;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public void setCurrencySymbol(String currencySymbol) {
    this.currencySymbol = currencySymbol;
  }

  public void setId(URI id) {
    this.id = id;
  }
  
  public void setLedgerPrefix(String ledgerPrefix) {
    this.ledgerPrefix = ledgerPrefix;
  }

  public void setNotificationSignPublicKey(String key) {
    this.notificationSignPublicKey = key;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public void setUrls(Map<String, URI> urls) {
    this.urls = urls;
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
