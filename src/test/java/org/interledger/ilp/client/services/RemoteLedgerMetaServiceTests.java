package org.interledger.ilp.client.services;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.ledger.client.rest.RestLedgerClient;
import org.interledger.ilp.ledger.client.rest.service.RestLedgerMetaService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class RemoteLedgerMetaServiceTests {

  private MockRestServiceServer mockServer;

  private RestTemplate restTemplate;

  @Before
  public void setup() {
    this.restTemplate = new RestTemplate();
    this.mockServer =
        MockRestServiceServer.bindTo(this.restTemplate).ignoreExpectOrder(true).build();
  }

  @Test
  public void getLedgerInfoSuccess() throws Exception {

    Resource responseBody = new ClassPathResource("default_ledger.json", this.getClass());

    this.mockServer.expect(requestTo("/")).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));
    
    Map<String, URI> urls = new HashMap<>();
    urls.put(RestLedgerClient.LEDGER_URL_NAME, URI.create("/"));

    RestLedgerMetaService service = new RestLedgerMetaService(restTemplate, urls);
    
    @SuppressWarnings("unused")
    LedgerInfo ledger = service.getLedgerInfo();
    
    this.mockServer.verify();    
  }

}
