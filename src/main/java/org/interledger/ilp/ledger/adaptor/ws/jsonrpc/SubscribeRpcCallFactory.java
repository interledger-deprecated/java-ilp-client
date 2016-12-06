package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.util.UriTemplate;

public class SubscribeRpcCallFactory {
  
  public static JsonRpcRequest build(String accountUrl, String... accounts) {
    JsonRpcSubscribeAccountRequest req = new JsonRpcSubscribeAccountRequest();
    req.setId(UUID.randomUUID().toString());
    
    List<URI> accParam = new ArrayList<>();
    
    for (String account : accounts) {
      accParam.add(new UriTemplate(accountUrl).expand(account));
    }
    
    JsonRpcSubscribeAccountRequestParams params = new JsonRpcSubscribeAccountRequestParams();
    params.setAccounts(accParam);
    params.setEventType("*");
    
    req.setParams(params);
    
    return req;
  }

}

