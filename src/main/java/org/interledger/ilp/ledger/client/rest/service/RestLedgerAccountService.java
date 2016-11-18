package org.interledger.ilp.ledger.client.rest.service;

import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.interledger.ilp.ledger.client.rest.json.JsonAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class RestLedgerAccountService extends RestServiceBase implements LedgerAccountService {

  private static final Logger log = LoggerFactory.getLogger(RestLedgerAccountService.class);

  @Override
  public Account getAccount(String name) throws HttpStatusCodeException, RestServiceException {
    try {

      log.debug("GET Account - name:" + name);

      return restTemplate.getForObject(this.serviceUrl, JsonAccount.class, name);

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
}
