package org.interledger.ilp.ledger.client.ws.jsonrpc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscribeRpcCallFactory {
  public static final String METHOD = "subscribe_account";
  
  public static JsonRpcRequest build(String... accounts) {
    JsonRpcRequest req = new JsonRpcRequest();
    req.setVersion("2.0"); //TODO:??
    req.setId(UUID.randomUUID().toString());
    req.setMethod(METHOD);
    
    List<URI> accParam = new ArrayList<>();
    
    for (String account : accounts) {
      accParam.add(URI.create("http://andrew-ilp/accounts/" + account));
    }
    
    SubscribeParams params = new SubscribeParams();
    params.setAccounts(accParam);
    params.setEventType("*");
    
    req.setParams(params);
    
    return req;
  }
  
  public static class SubscribeParams {
    
    private String eventType;
    private List<URI> accounts;
    
    @JsonProperty(value = "eventType")
    public String getEventType() {
      return eventType;
    }
    
    @JsonProperty(value = "accounts")
    public List<URI> getAccounts() {
      return accounts;
    }
    
    public void setEventType(String eventType) {
      this.eventType = eventType;
    }
    
    public void setAccounts(List<URI> accounts) {
      this.accounts = accounts;
    }
  }
}

