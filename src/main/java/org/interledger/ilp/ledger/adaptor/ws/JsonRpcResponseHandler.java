package org.interledger.ilp.ledger.adaptor.ws;

import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponseMessage;

@FunctionalInterface
public interface JsonRpcResponseHandler {

  void handleResponse(JsonRpcRequestMessage request, JsonRpcResponseMessage response);
  
}
