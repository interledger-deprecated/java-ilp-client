package org.interledger.ilp.ledger.adaptor.rest.service;

import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.RestServiceException;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestServiceBase {

  protected static final Logger log = LoggerFactory.getLogger(RestServiceBase.class);

  protected RestTemplate restTemplate;
  protected RestLedgerAdaptor adaptor;
  
  public RestServiceBase(RestLedgerAdaptor adaptor, RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.adaptor = adaptor;
  }

  public RestTemplate getRestTemplate() {
    return this.restTemplate;
  }
  
  public String getServiceUrl(ServiceUrls name) {
    return adaptor.getServiceUrl(name);
  }

  protected RestServiceException parseRestException(HttpStatusCodeException knownException) {
    JsonError error;
    try {
      error = JsonError.fromJson(knownException.getResponseBodyAsString());
    } catch (Exception e) {
      // Can't parse JSON
      error = new JsonError();
      error.setId("-1");
      error.setId(
          "Unknown RestServiceException, unable to parse details from response. See innerException for raw response.");
    }
    return new RestServiceException(error, knownException);
  }



}
