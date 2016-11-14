package org.interledger.messages;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoteLedgerUrls {

	private URI transferUrl;
	private URI transferFulfillmentUrl;
	private URI transferRejectionUrl;
	private URI accountUrl;
	private URI accountTransfersUrl;
	private URI messageUrl;
	
	@JsonProperty(value = "transfer")
	public URI getTransferUrl() {
		return transferUrl;
	}
	
	@JsonProperty(value = "transfer_fulfillment")
	public URI getTransferFulfillmentUrl() {
		return transferFulfillmentUrl;
	}
	
	@JsonProperty(value = "transfer_rejection")
	public URI getTransferRejectionUrl() {
		return transferRejectionUrl;
	}
	
	@JsonProperty(value = "account")
	public URI getAccountUrl() {
		return accountUrl;
	}
	
	@JsonProperty(value = "account_tranfers")
	public URI getAccountTransfersUrl() {
		return accountTransfersUrl;
	}
	
	@JsonProperty(value = "message")
	public URI getMessageUrl() {
		return messageUrl;
	}
	
	public void setTransferUrl(URI transferUrl) {
		this.transferUrl = transferUrl;
	}
	
	public void setTransferFulfillmentUrl(URI transferFulfillmentUrl) {
		this.transferFulfillmentUrl = transferFulfillmentUrl;
	}
	
	public void setTransferRejectionUrl(URI transferRejectionUrl) {
		this.transferRejectionUrl = transferRejectionUrl;
	}
	
	public void setAccountUrl(URI accountUrl) {
		this.accountUrl = accountUrl;
	}
	
	public void setAccountTransfersUrl(URI accountTransfersUrl) {
		this.accountTransfersUrl = accountTransfersUrl;
	}
	
	public void setMessageUrl(URI messageUrl) {
		this.messageUrl = messageUrl;
	}
}
