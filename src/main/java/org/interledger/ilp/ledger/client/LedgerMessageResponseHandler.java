package org.interledger.ilp.ledger.client;

import org.interledger.ilp.core.ledger.model.LedgerMessage;

@FunctionalInterface
public interface LedgerMessageResponseHandler {

  void handleResponse(LedgerMessage request, LedgerMessage response, Throwable error);
  
}
