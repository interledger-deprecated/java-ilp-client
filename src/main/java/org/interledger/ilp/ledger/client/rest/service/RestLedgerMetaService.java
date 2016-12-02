package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;
import java.util.Map;

import org.interledger.ilp.ledger.client.rest.RestLedgerClient;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerMetaService extends RestServiceBase {

  public RestLedgerMetaService(RestTemplate restTemplate, Map<String, URI> urls) {
    super(restTemplate, urls);
  }
  
  public JsonLedgerInfo getLedgerInfo() {
    
    try {

      log.debug("GET Meta");
      return restTemplate.getForObject(
          getServiceUrl(RestLedgerClient.LEDGER_URL_NAME),
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
