package org.interledger.messages;

import org.interledger.core.LedgerInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoteLedgerInfo implements LedgerInfo {
	
	private int precision;
	private int scale;
	private String currencyCode;
	private String currencySymbol;
	private String baseUri;
	private RemoteLedgerUrls urls;

	@Override
	@JsonProperty(value = "precision")
	public int getPrecision() {
		return precision;
	}

	@Override
	@JsonProperty(value = "scale")
	public int getScale() {
		return scale;
	}

	@Override
	@JsonProperty(value = "currency_code")
	public String getCurrencyCode() {
		return currencyCode;
	}

	@Override
	@JsonProperty(value = "currency_symbol")
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	@Override
	@JsonProperty(value = "ilp_prefix")
	public String getBaseUri() {
		return baseUri;
	}
	
	@JsonProperty(value = "urls")
	public RemoteLedgerUrls getUrls() {
		return urls;
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

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	
	public void setUrls(RemoteLedgerUrls urls) {
		this.urls = urls;
	}

	@Override
	public String toString() {
		return getBaseUri() + " : " + getCurrencySymbol() + " : " + getCurrencyCode(); 
	}
	
}
