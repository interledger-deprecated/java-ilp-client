package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcSubscribeAccountRequestParams implements JsonRpcRequestParams {
    
  private List<URI> accounts;  
  private String eventType;
  
  @JsonProperty(value = "eventType")
  public String getEventType() {
    return eventType;
  }
  
  @JsonProperty(value = "accounts")
  public List<URI> getAccounts() {
    return this.accounts;
  }
  
  public void setEventType(String eventType) {
    this.eventType = eventType;
  }
  
  public void setAccounts(List<URI> accParam) {
    this.accounts = accParam;
  }

}
