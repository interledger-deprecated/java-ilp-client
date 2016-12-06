package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event")
@JsonSubTypes({
    @Type(value = JsonRpcRequestTransferNotificationParams.class, name = "transfer.create"),
    @Type(value = JsonRpcRequestTransferNotificationParams.class, name = "transfer.update"),
    @Type(value = JsonRpcRequestMessageNotificationParams.class, name = "message.send")})
public abstract class JsonRpcNotificationParams {

  private UUID id;
  private String event;
  private Map<String, String> relatedResources;

  @JsonProperty(value = "id")
  public UUID getId() {
    return this.id;
  }
   
  @JsonProperty(value = "event")
  public String getEvent() {
    return this.event;
  }
    
  @JsonProperty(value = "related_resources")
  public Map<String, String> getRelatedResources() {
    return this.relatedResources;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public void setRelatedResources(Map<String, String> relatedResources) {
    this.relatedResources = relatedResources;
  }

  
}
