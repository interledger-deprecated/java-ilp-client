package org.interledger.ilp.ledger.adaptor.rest;

public enum ServiceUrls {
  LEDGER("ledger"),
  HEALTH("health"),
  TRANSFER("transfer"),
  TRANSFER_REJECTION("transfer_rejection"),
  TRANSFER_FULFILLMENT("transfer_fulfillment"),
  TRANSFER_STATE("transfer_state"),
  ACCOUNT("account"),
  ACCOUNTS("accounts"),
  AUTH_TOKEN("auth_token"),
  WEBSOCKET("websocket"),
  MESSAGE("message");
   
  private String name;

  private ServiceUrls(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public static ServiceUrls fromName(String name) {
    for (ServiceUrls url : ServiceUrls.values()) {
      if(url.getName().equals(name)) {
        return url;
      }
    }
    throw new RuntimeException("Unknown URL name: " + name);
  }
  
}

