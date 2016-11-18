package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.core.ledger.service.LedgerMetaService;
import org.interledger.ilp.core.ledger.service.LedgerServiceFactory;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class RestLedgerServiceFactory implements LedgerServiceFactory {

  private URI ledgerBaseUrl;

  private LedgerAccountService accountService;
  private LedgerMetaService metaService;

  private RestTemplateBuilder restTemplateBuilder;

  private UsernamePasswordAuthenticationToken accountAuthToken;

  public RestLedgerServiceFactory(RestTemplateBuilder restTemplateBuilder, String ledgerBaseUrl)
      throws URISyntaxException {

    this.restTemplateBuilder = restTemplateBuilder;
    this.ledgerBaseUrl = new URI(ledgerBaseUrl);

  }

  private RestTemplateBuilder getRestTemplateBuilderWithAuthIfAvailable() {

    if (accountAuthToken != null
        && (accountAuthToken.getPrincipal() != null && accountAuthToken.getCredentials() != null)) {

      return restTemplateBuilder.basicAuthorization(accountAuthToken.getPrincipal().toString(),
          accountAuthToken.getCredentials().toString());

    }

    return restTemplateBuilder;

  }

  @Override
  public LedgerAccountService getAccountService() throws Exception {

    // The service is Autowired but may not be configured
    if (accountService instanceof RestServiceBase) {

      // If no URL is configured then use the one fetched from the meta service
      if (((RestServiceBase) accountService).getServiceUrl() == null) {
        LedgerInfo info = getMetaService().getLedgerInfo();

        if (info instanceof JsonLedgerInfo) {
          String accountUrl = ((JsonLedgerInfo) info).getUrls().getAccountUrl().toString();
          ((RestServiceBase) accountService).setServiceUrl(accountUrl);
        }
      }

      // If no RestTemplate is configured use the builder
      if (((RestServiceBase) accountService).getRestTemplate() == null) {
        ((RestServiceBase) accountService)
            .setRestTemplate(getRestTemplateBuilderWithAuthIfAvailable().build());
      }
    }

    return accountService;
  }

  @Override
  public LedgerMetaService getMetaService() throws Exception {

    // The service is Autowired but may not be configured
    if (metaService instanceof RestServiceBase) {

      // If no URL is configured then build from the ledger's base
      if (((RestServiceBase) metaService).getServiceUrl() == null) {

        // TODO Make "/ledger" configurable
        URI metaUri = ledgerBaseUrl.resolve("/ledger");
        ((RestServiceBase) metaService).setServiceUrl(metaUri.toString());
      }

      // If no RestTemplate is configured use the builder
      if (((RestServiceBase) metaService).getRestTemplate() == null) {
        ((RestServiceBase) metaService)
            .setRestTemplate(getRestTemplateBuilderWithAuthIfAvailable().build());
      }
    }

    return metaService;
  }

  @Autowired(required = false)
  public void setAccountAuthToken(UsernamePasswordAuthenticationToken accountAuthToken) {
    this.accountAuthToken = accountAuthToken;
  }

  @Autowired
  public void setAccountService(LedgerAccountService accountService) {
    this.accountService = accountService;
  }

  @Autowired
  public void setMetaService(LedgerMetaService metaService) {
    this.metaService = metaService;
  }

}
