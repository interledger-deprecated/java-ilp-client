package org.interledger.ilp.ledger.client.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.interledger.ilp.core.ledger.LedgerClient;
import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.model.Message;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.core.ledger.service.LedgerNotificationListenerService;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.interledger.ilp.ledger.client.rest.service.JsonRpcLedgerNotificationListenerService;
import org.interledger.ilp.ledger.client.rest.service.RestLedgerAccountService;
import org.interledger.ilp.ledger.client.rest.service.RestLedgerMessageService;
import org.interledger.ilp.ledger.client.rest.service.RestLedgerMetaService;
import org.interledger.ilp.ledger.client.rest.service.RestLedgerTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class RestLedgerClient implements LedgerClient, ApplicationContextAware {

  public static String LEDGER_URL_NAME = "ledger";
  public static final String WEBSOCKET_URL_NAME = "websocket";
  private static final Logger log = LoggerFactory.getLogger(RestLedgerClient.class);
  
  private ApplicationContext context;
  
  private Map<String, URI> urls;
  private JsonLedgerInfo ledgerInfo = null;
  private UsernamePasswordAuthenticationToken accountAuthToken = null;
  
  private RestTemplateBuilder restTemplateBuilder;
  private RestLedgerMetaService metaService;
  private RestLedgerAccountService accountService;
  private RestLedgerTransferService transferService;
  private JsonRpcLedgerNotificationListenerService notificationListenerService;
  private RestLedgerMessageService messageService;

  public RestLedgerClient(RestTemplateBuilder restTemplateBuilder, String ledgerBaseUrl)
      throws URISyntaxException {

    this.restTemplateBuilder = restTemplateBuilder;
    urls = new HashMap<String, URI>();
    urls.put(LEDGER_URL_NAME, URI.create(ledgerBaseUrl));
    
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
  public LedgerInfo getLedgerInfo() throws Exception {
    
    throwIfNotConnected();
    
    return ledgerInfo;
  }

  @Override
  public LedgerAccountService getAccountService() throws Exception {

    throwIfNotConnected();
    
    if(this.accountService == null) {
      log.debug("Creating Account Service");
      this.accountService = new RestLedgerAccountService(getRestTemplateBuilderWithAuthIfAvailable().build(), urls);
    }
    
    return this.accountService;
    
  }
  
  @Override
  public LedgerTransferService getTransferService() throws Exception {

    throwIfNotConnected();
    
    if(this.transferService == null) {
      log.debug("Creating Transfer Service");
      this.transferService = new RestLedgerTransferService(getRestTemplateBuilderWithAuthIfAvailable().build(), urls);
    }
    
    return this.transferService;
    
  }
  
  @Override
  public LedgerNotificationListenerService getLedgerNotificationListenerService() throws Exception {
    
    throwIfNotConnected();
    
    if(this.notificationListenerService == null) {
      log.debug("Creating Notification Listener Service");
      URI wsUri = urls.get(WEBSOCKET_URL_NAME);
      
      if(wsUri == null || wsUri.getScheme() == null  || !wsUri.getScheme().startsWith("ws")) {
        throw new Exception("Invalid websocket URL: " + wsUri);
      }
      
      this.notificationListenerService = new JsonRpcLedgerNotificationListenerService(wsUri.toString());
    }
    
    return this.notificationListenerService;
  }

  @Autowired(required = false)
  public void setAccountAuthToken(UsernamePasswordAuthenticationToken accountAuthToken) {
    this.accountAuthToken = accountAuthToken;
  }
  
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  @Override
  public void connect() throws Exception {
    if(metaService == null) {
      metaService = new RestLedgerMetaService(restTemplateBuilder.build(), urls);
    }
    ledgerInfo = metaService.getLedgerInfo();
    urls.putAll(ledgerInfo.getUrls());
  }
  
  public boolean isConnected() {
    return (this.ledgerInfo != null);
  }

  @Override
  public void disconnect() throws Exception {
    
    throwIfNotConnected();
    
    //TODO - ensure the notification listener is closed
    
    ledgerInfo = null;
    URI ledgerUri = urls.get(LEDGER_URL_NAME);
    urls.clear();
    urls.put(LEDGER_URL_NAME, ledgerUri);
    
  }

  @Override
  public void sendMessage(Message msg) throws Exception {

    throwIfNotConnected();
    
    if(messageService == null) {
      messageService = new RestLedgerMessageService(getRestTemplateBuilderWithAuthIfAvailable().build(), urls);
    }
    
    messageService.sendMessage(msg);
    
  }
  
  private void throwIfNotConnected() throws Exception {
    if(!isConnected()) {
      throw new Exception("Client is not connected.");
    }
  }
  
}
