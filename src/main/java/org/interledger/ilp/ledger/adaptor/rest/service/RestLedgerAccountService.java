package org.interledger.ilp.ledger.adaptor.rest.service;

import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.exceptions.RestServiceException;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonAccount;
import org.interledger.ilp.ledger.adaptor.ws.JsonRpcLedgerWebSocketChannel;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequest;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.SubscribeRpcCallFactory;
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
  public void subscribeToAccountNotifications(String accountName) throws Exception {
    
    if (!websocketChannel.canSend()) {
      throw new Exception("Websocket is disconnected. No session available to send.");
    }
    
    JsonRpcRequest subReq = SubscribeRpcCallFactory.build(adaptor.getServiceUrl(ServiceUrls.ACCOUNT), accountName);
    websocketChannel.sendRpcRequest(subReq);
  }  

}
