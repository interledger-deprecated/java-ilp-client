package org.interledger.ilp.ledger.client.model;

public class ClientQuoteQuery {
  private String method;
  
  private String id;
  
  private ClientQuoteQueryParams data;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ClientQuoteQueryParams getData() {
    return data;
  }

  public void setData(ClientQuoteQueryParams data) {
    this.data = data;
  }
  
  
}

