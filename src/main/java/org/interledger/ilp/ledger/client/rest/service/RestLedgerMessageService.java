package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.interledger.ilp.core.ledger.model.Message;
import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.interledger.ilp.ledger.model.impl.MessageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
      
      MessageImpl nativeMsg = mapToNative(msg);
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<Message> request = new HttpEntity<Message>(nativeMsg, headers);
      
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

  protected MessageImpl mapToNative(Message msg) {
    //we need this awkward method to map from human friendly commands into the REST format,
    //no one wants to type in full urls for account names...
    MessageImpl impl = new MessageImpl();
    
    impl.setFrom(urls.get("account").toString().replace(":name", msg.getFromAccount()));
    impl.setTo(urls.get("account").toString().replace(":name", msg.getToAccount()));
    impl.setLedger(urls.get("ledger").toString());
    
    //the ledger wants a json object as a convenience, for now...
    Map<String, Object> data = new HashMap<>();
    data.put("message", msg.getData());
    
    impl.setData(data);
    
    return impl;
  }

}
