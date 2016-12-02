package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;
import java.util.Map;

import org.interledger.ilp.core.ledger.model.Message;
import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerMessageService extends RestServiceBase {

  public static final String MESSAGE_URL_NAME = "message";

  public RestLedgerMessageService(RestTemplate restTemplate, Map<String, URI> urls) {
    super(restTemplate, urls);
  }
  
  public void sendMessage(Message msg) throws RestServiceException {
    
    try {

      log.debug("POST message");
      
      HttpEntity<Message> request = new HttpEntity<Message>(msg);
      request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
      
      restTemplate.postForEntity(
          getServiceUrl(MESSAGE_URL_NAME),
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

}
