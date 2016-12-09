package org.interledger.ilp.ledger.adaptor.rest.service;

import java.net.URI;

import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.RestServiceException;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerMessage;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

public class RestLedgerMessageService extends RestServiceBase {

  public RestLedgerMessageService(RestLedgerAdaptor adaptor, RestTemplate restTemplate) {
    super(adaptor, restTemplate);
  }
  
  public void sendMessage(LedgerMessage msg) throws RestServiceException {
    
    try {

      log.debug("POST message");
      
      //TODO: not sure if this is correct or not - can we treat from and to accounts as placeholders
      //to wrap into REST account uri's? What about setting the ledger - can you send messages 
      //across ledgers? (at the moment we simply ignore the ledger setting in LedgerMessage and use
      //the REST ledger url).
      JsonLedgerMessage jsonMessage = buildJsonLedgerMessage(msg);
      
      RequestEntity<LedgerMessage> request = RequestEntity.post(
            URI.create(getServiceUrl(ServiceUrls.MESSAGE)))
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .body(jsonMessage, JsonLedgerMessage.class);
      
      restTemplate.postForEntity(
          getServiceUrl(ServiceUrls.MESSAGE),
          request,
          String.class);
      
    } catch (HttpStatusCodeException e) {
      switch (e.getStatusCode()) {
        case BAD_REQUEST:
        case UNPROCESSABLE_ENTITY:
         throw parseRestException(e);
        default:
          throw e;
      }
    }
    
  }

  protected JsonLedgerMessage buildJsonLedgerMessage(LedgerMessage message) {
    JsonLedgerMessage jsonMessage = new JsonLedgerMessage();
    
    jsonMessage.setLedger(getServiceUrl(ServiceUrls.LEDGER));
    jsonMessage.setFrom(new UriTemplate(getServiceUrl(ServiceUrls.ACCOUNT)).expand(message.getFrom()).toString());
    jsonMessage.setTo(new UriTemplate(getServiceUrl(ServiceUrls.ACCOUNT)).expand(message.getTo()).toString());
    jsonMessage.setData(message.getData());
    
    return jsonMessage;
  }

}
