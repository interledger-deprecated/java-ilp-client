package org.interledger.ilp.ledger.adaptor.rest.service;

import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerInfo;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerMetaService extends RestServiceBase {

  public RestLedgerMetaService(RestLedgerAdaptor adaptor, RestTemplate restTemplate) {
    super(adaptor, restTemplate);
  }
  
  public JsonLedgerInfo getLedgerInfo() {
    
    try {

      log.debug("GET Meta");
      return restTemplate.getForObject(
          getServiceUrl(ServiceUrls.LEDGER),
          JsonLedgerInfo.class);
      
    } catch (HttpStatusCodeException e) {
      switch (e.getStatusCode()) {
        // No known RestExceptions for the metadata service
        // case BAD_REQUEST:
        // throw parseRestException(e);
        default:
          throw e;
      }
    }
    
  }

}
