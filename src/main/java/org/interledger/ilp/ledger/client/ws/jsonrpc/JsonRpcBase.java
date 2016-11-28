package org.interledger.ilp.ledger.client.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcBase {
  
  private String version;
  private String id;
  private String method;
  private JsonRpcParams params;
  private JsonRpcError error;

  @JsonProperty(value = "jsonrpc", defaultValue = "2.0")
  public String getVersion() {
    return version;
  }

  @JsonProperty(value = "id")
  public String getId() {
    return id;
  }

  @JsonProperty(value = "method")
  public String getMethod() {
    return method;
  }

  @JsonProperty(value = "params")
  public JsonRpcParams getParams() {
    return params;
  }

  @JsonProperty(value = "error")
  public JsonRpcError getError() {
    return error;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setParams(JsonRpcParams params) {
    this.params = params;
  }

  public void setError(JsonRpcError error) {
    this.error = error;
  }

  public class JsonRpcParams {
    
    private String event;
    private Object resource;
    
    @JsonProperty(value = "event")
    public String getEvent() {
      return event;
    }
    
    @JsonProperty(value = "resource")
    public Object getResource() {
      return resource;
    }
    
    public void setEvent(String event) {
      this.event = event;
    }
    
    public void setResource(Object resource) {
      this.resource = resource;
    }
    
  }
  
  public class JsonRpcError {
    
    private int code;
    private String message;
    private String data;
    
    @JsonProperty(value = "code")
    public int getCode() {
      return code;
    }
    
    @JsonProperty(value = "message")
    public String getMessage() {
      return message;
    }
    
    @JsonProperty(value = "data")
    public String getData() {
      return data;
    }
    
    public void setCode(int code) {
      this.code = code;
    }
    
    public void setMessage(String message) {
      this.message = message;
    }
    
    public void setData(String data) {
      this.data = data;
    }
    
  }

}
