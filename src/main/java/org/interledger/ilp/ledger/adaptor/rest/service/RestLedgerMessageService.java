package org.interledger.ilp.ledger.adaptor.rest.service;

import java.net.URI;

import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.RestServiceException;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerMessageService extends RestServiceBase {

  public RestLedgerMessageService(RestLedgerAdaptor adaptor, RestTemplate restTemplate) {
    super(adaptor, restTemplate);
  }
  
  public void sendMessage(LedgerMessage msg) throws RestServiceException {
    
    try {

      log.debug("POST message");
      
      RequestEntity<LedgerMessage> request = RequestEntity.post(
            URI.create(getServiceUrl(ServiceUrls.MESSAGE)))
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .body(msg, JsonLedgerMessage.class);
      
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

  /*
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
  */

}
