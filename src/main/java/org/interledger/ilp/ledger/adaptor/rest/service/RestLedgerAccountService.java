package org.interledger.ilp.ledger.adaptor.rest.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.AdaptorStateException;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.RestServiceException;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonAccount;
import org.interledger.ilp.ledger.adaptor.ws.JsonRpcLedgerWebSocketChannel;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponseMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcSubscribeAccountRequest;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcSubscribeAccountRequestParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerAccountService extends RestServiceBase implements LedgerAccountService {

  private JsonRpcLedgerWebSocketChannel websocketChannel;

  public RestLedgerAccountService(RestLedgerAdaptor adaptor, RestTemplate restTemplate, JsonRpcLedgerWebSocketChannel websocketChannel) {
    super(adaptor, restTemplate);
    this.websocketChannel = websocketChannel;
  }

  private static final Logger log = LoggerFactory.getLogger(RestLedgerAccountService.class);

  @Override
  public Account getAccount(String name) throws HttpStatusCodeException, RestServiceException {
    try {

      log.debug("GET Account - name:" + name);

      return restTemplate.getForObject(getServiceUrl(ServiceUrls.ACCOUNT), JsonAccount.class, name);

    } catch (HttpStatusCodeException e) {
      switch (e.getStatusCode()) {
        case BAD_REQUEST:
        case NOT_FOUND:
          throw parseRestException(e);
        default:
          throw e;
      }
    }
  }
  
  @Override
  public void subscribeToAccountNotifications(String accountName) throws AdaptorStateException {
    
    if (!websocketChannel.canSend()) {
      throw new AdaptorStateException("Websocket is disconnected. No session available to send.");
    }
    
    log.debug("Subscribing to notifications for: " + accountName);
    
    //TODO JsonRpcRequests could come from a factory
    JsonRpcSubscribeAccountRequest subscribeRequest = new JsonRpcSubscribeAccountRequest();
    subscribeRequest.setId(UUID.randomUUID().toString());
    
    JsonRpcSubscribeAccountRequestParams params = new JsonRpcSubscribeAccountRequestParams();
    subscribeRequest.setParams(params); 
    
    List<URI> accParam = new ArrayList<>();
    params.setAccounts(accParam);
    accParam.add(adaptor.getAccountIdentifier(accountName));

    params.setEventType("*");
            
    websocketChannel.sendRpcRequest(subscribeRequest, (request, response) -> {
      if(response.getError() != null) {
        log.error("JsonRpcResponseMessage error: " + response.getError().getMessage());
        return;
      }
      
      if(JsonRpcResponseMessage.isSuccess(response)) {
        log.info("Subscribed to notifications for: " + accountName);
      }
      else
      {
        log.error("Unable to subscribe to notifications for: " + accountName);
      }
      
      log.debug(response.getResult().toString());
      
    });
  }  

}
