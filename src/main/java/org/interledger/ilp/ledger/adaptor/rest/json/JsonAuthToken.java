package org.interledger.ilp.ledger.adaptor.rest.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonAuthToken {
  
  private String token;
  
  @JsonProperty(value = "token")
  public String getToken() {
    return this.token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
