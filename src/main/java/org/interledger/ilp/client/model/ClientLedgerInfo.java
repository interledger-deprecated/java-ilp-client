package org.interledger.ilp.client.model;

import java.security.PublicKey;

import javax.money.CurrencyUnit;
import javax.money.format.MonetaryAmountFormat;

import org.interledger.ilp.InterledgerAddress;
import org.interledger.ilp.ledger.model.LedgerInfo;

public class ClientLedgerInfo implements LedgerInfo {

  private String id;
  private InterledgerAddress prefix;
  private int precision;
  private int scale;
  private CurrencyUnit currency;
  private MonetaryAmountFormat format;
  private PublicKey conditionSignPublicKey;
  private PublicKey notificationSignPublicKey;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public InterledgerAddress getAddressPrefix() {
    return prefix;
  }

  @Override
  public int getPrecision() {
    return precision;
  }

  @Override
  public int getScale() {
    return scale;
  }

  @Override
  public CurrencyUnit getCurrencyUnit() {
    return currency;
  }
  
  @Override
  public MonetaryAmountFormat getMonetaryAmountFormat() {
    return format;
  }

  @Override
  public PublicKey getConditionSignPublicKey() {
    return conditionSignPublicKey;
  }

  @Override
  public PublicKey getNotificationSignPublicKey() {
    return notificationSignPublicKey;
  }

  public InterledgerAddress getPrefix() {
    return prefix;
  }

  public CurrencyUnit getCurrency() {
    return currency;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setPrefix(InterledgerAddress prefix) {
    this.prefix = prefix;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public void setCurrencyUnit(CurrencyUnit currency) {
    this.currency = currency;
  }

  public void setMonetaryAmountFormat(MonetaryAmountFormat format) {
    this.format = format;
  }

  public void setConditionSignPublicKey(PublicKey conditionSignPublicKey) {
    this.conditionSignPublicKey = conditionSignPublicKey;
  }

  public void setNotificationSignPublicKey(PublicKey notificationSignPublicKey) {
    this.notificationSignPublicKey = notificationSignPublicKey;
  }

}
