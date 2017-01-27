package org.interledger.ilp.client;

import org.interledger.ilp.ledger.model.LedgerMessage;

@FunctionalInterface
public interface LedgerMessageResponseHandler {

  void handleResponse(LedgerMessage request, LedgerMessage response, Throwable error);
  
}
