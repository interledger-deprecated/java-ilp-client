package org.interledger.ilp.ledger.adaptor.rest.service;

import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.RestServiceException;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonAuthToken;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerAuthTokenService extends RestServiceBase {

  public RestLedgerAuthTokenService(RestLedgerAdaptor adaptor, RestTemplate restTemplate) {
    super(adaptor, restTemplate);
  }
  
  public String getAuthToken() throws RestServiceException {
    
    try {

      log.debug("GET Auth Token");
      JsonAuthToken token = restTemplate.getForObject(
          getServiceUrl(ServiceUrls.AUTH_TOKEN),
          JsonAuthToken.class);
      return token.getToken();
      
    } catch (HttpStatusCodeException e) {
      switch (e.getStatusCode()) {
        case UNAUTHORIZED:
          throw parseRestException(e);
        default:
          throw e;
      }
    }
    
  }

}
