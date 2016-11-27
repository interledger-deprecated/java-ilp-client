package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.core.ledger.service.LedgerMetaService;
import org.interledger.ilp.core.ledger.service.LedgerServiceFactory;
import org.interledger.ilp.core.ledger.service.LedgerTransferRejectionService;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class RestLedgerServiceFactory implements LedgerServiceFactory {

  private URI ledgerBaseUrl;

  private LedgerAccountService accountService;
  private LedgerMetaService metaService;
  private LedgerTransferService transferService;
  private LedgerTransferRejectionService transferRejectionService;

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
  public LedgerTransferRejectionService getTransferRejectionService() throws Exception {
    // The service is Autowired but may not be configured
    if (transferRejectionService instanceof RestLedgerTransferRejectionService) {
      // If no URL is configured then use the one fetched from the meta service
      if (((RestServiceBase) transferRejectionService).getServiceUrl() == null) {
        LedgerInfo info = getMetaService().getLedgerInfo();

        if (info instanceof JsonLedgerInfo) {
          String rejectionUrl = ((JsonLedgerInfo) info).getUrls().getTransferRejectionUrl()
              .toString();
          ((RestServiceBase) transferRejectionService).setServiceUrl(rejectionUrl);
        }
      }

      // If no RestTemplate is configured use the builder
      if (((RestServiceBase) transferRejectionService).getRestTemplate() == null) {
        ((RestServiceBase) transferRejectionService)
            .setRestTemplate(getRestTemplateBuilderWithAuthIfAvailable().build());
      }
    }

    return transferRejectionService;
  }
  
  @Override
  public LedgerTransferService getTransferService() throws Exception {

    // The service is Autowired but may not be configured
    if (transferService instanceof RestLedgerTransferService) {

      RestLedgerTransferService txService = (RestLedgerTransferService) transferService;

      // If no URL is configured then use the one fetched from the meta
      // service
      if (txService.getServiceUrl() == null || txService.getAccountUrl() == null) {

        LedgerInfo info = getMetaService().getLedgerInfo();

        if (info instanceof JsonLedgerInfo) {

          JsonLedgerUrls urls = ((JsonLedgerInfo) info).getUrls();

          if (txService.getServiceUrl() == null) {
            txService.setServiceUrl(urls.getTransferUrl().toString());
          }

          if (txService.getAccountUrl() == null) {
            txService.setAccountUrl(urls.getAccountUrl().toString());
          }
        }
      }

      if (txService.getLedgerUrl() == null) {
        txService.setLedgerUrl(ledgerBaseUrl.toString());
      }

      // If no RestTemplate is configured use the builder
      if (txService.getRestTemplate() == null) {
        txService.setRestTemplate(getRestTemplateBuilderWithAuthIfAvailable().build());
      }
    }
    return transferService;
  }

	@Override
  public LedgerMetaService getMetaService() throws Exception {

    // The service is Autowired but may not be configured
    if (metaService instanceof RestServiceBase) {

      // If no URL is configured then build from the ledger's base
      if (((RestServiceBase) metaService).getServiceUrl() == null) {
        ((RestServiceBase) metaService).setServiceUrl(ledgerBaseUrl.toString());
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

  @Autowired
  public void setTransferService(LedgerTransferService transferService) {
    this.transferService = transferService;
  } 
  
  @Autowired
  public void setTransferRejectionService(LedgerTransferRejectionService transferRejectionService) {
    this.transferRejectionService = transferRejectionService;
  }
}
