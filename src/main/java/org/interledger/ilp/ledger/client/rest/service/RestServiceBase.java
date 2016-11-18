package org.interledger.ilp.ledger.client.rest.service;

import java.util.regex.Pattern;

import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.interledger.ilp.ledger.client.rest.json.JsonError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestServiceBase {

  protected static final Logger log = LoggerFactory.getLogger(RestServiceBase.class);

  private static final Pattern regex = Pattern.compile("\\:([A-Za-z0-9-]+)");

  protected RestTemplate restTemplate;
  protected String serviceUrl;

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getServiceUrl() {
    return this.serviceUrl;
  }

  public RestTemplate getRestTemplate() {
    return this.restTemplate;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = fixUriTemplates(serviceUrl);
  }

  private String fixUriTemplates(String input) {
    return regex.matcher(input.toString()).replaceAll("\\{$1\\}");
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
