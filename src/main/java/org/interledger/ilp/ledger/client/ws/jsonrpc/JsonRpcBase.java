package org.interledger.ilp.ledger.client.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcBase {
  
  private String version;
  private String id;

  @JsonProperty(value = "jsonrpc", defaultValue = "2.0")
  public String getVersion() {
    return version;
  }

  @JsonProperty(value = "id")
  public String getId() {
    return id;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setId(String id) {
    this.id = id;
  }
}
