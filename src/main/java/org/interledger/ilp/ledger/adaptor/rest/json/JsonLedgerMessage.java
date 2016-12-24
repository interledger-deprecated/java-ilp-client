package org.interledger.ilp.ledger.adaptor.rest.json;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Map;

import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.client.model.ClientLedgerMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(Include.NON_NULL)
public class JsonLedgerMessage {

  private URI from;
  private URI to;
  private URI ledger;
  
  @JsonRawValue
  private Object data;
  
  @JsonProperty(value="ledger")
  public URI getLedger() {
    return this.ledger;
  }

  @JsonProperty(value="from")
  public URI getFrom() {
    return this.from;
  }

  @JsonProperty(value="to")
  public URI getTo() {
    return this.to;
  }

  @JsonProperty(value="data")
  public Object getData() {
    return this.data;
  }

  public void setFrom(URI from) {
    this.from = from;
  }

  public void setTo(URI to) {
    this.to = to;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public void setLedger(URI ledger) {
    this.ledger = ledger;
  }
  
  @Deprecated
  public void setAccount(String account) {
    
  }
  
  public LedgerMessage toLedgerMessage() {
    ClientLedgerMessage clientMessage = new ClientLedgerMessage();
    clientMessage.setLedger(getLedger().toString());
    clientMessage.setFrom(getFrom().toString());
    clientMessage.setTo(getTo().toString());
    Object data = getData();
    if(data instanceof Map) {
      try {
        ObjectMapper mapper = new ObjectMapper();
        String dataValue = mapper.writeValueAsString(data);
        clientMessage.setData(dataValue.getBytes(Charset.forName("UTF-8")));
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Unable to reserialize message data.", e);
      }
    } else {
      clientMessage.setData(Base64.getDecoder().decode(data.toString()));
    }
    return clientMessage;    
  }
  
  public static JsonLedgerMessage fromLedgerMessage(LedgerMessage message, RestLedgerAdaptor ledgerAdaptor) {
    
    JsonLedgerMessage jsonMessage = new JsonLedgerMessage();
    jsonMessage.setLedger(URI.create(message.getLedger() != null ? message.getLedger() : ledgerAdaptor.getServiceUrl(ServiceUrls.LEDGER)));
    jsonMessage.setFrom(ledgerAdaptor.getAccountIdentifier(message.getFrom()));
    jsonMessage.setTo(ledgerAdaptor.getAccountIdentifier(message.getTo()));
    
    //TODO Undocumented assumptions made here. 
    // If the provided data is valid UTF8 JSON then embed otherwise base64url encode and send as { "base64url" : "<data>"}. 
    String data = new String(message.getData(), Charset.forName("UTF-8"));
    if(JsonValidator.isValid(data)) {
      jsonMessage.setData(data);
    }
    else
    {
      jsonMessage.setData("{\"base64url\":\"" + Base64.getUrlEncoder().encodeToString(message.getData()) + "\"}");
    }
    return jsonMessage;    
  }
  
}
