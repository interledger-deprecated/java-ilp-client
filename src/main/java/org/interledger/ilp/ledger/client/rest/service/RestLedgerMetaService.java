package org.interledger.ilp.ledger.client.rest.service;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.service.LedgerMetaService;
import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class RestLedgerMetaService extends RestServiceBase implements LedgerMetaService {

  private LedgerInfo cache;

  @Override
  public LedgerInfo getLedgerInfo() throws HttpStatusCodeException, RestServiceException {

    if (cache == null) {
      try {

        log.debug("GET Meta");

        LedgerInfo ledgerInfo =
            restTemplate.getForObject(this.serviceUrl, JsonLedgerInfo.class);

        cache = ledgerInfo;

      } catch (HttpStatusCodeException e) {
        switch (e.getStatusCode()) {
          // No known RestExceptions for the metadata service
          // case BAD_REQUEST:
          // throw parseRestException(e);
          default:
            throw e;
        }
      }
    } else {
      log.debug("CACHED Meta");
    }

    // TODO Safe copy
    return cache;
  }

}
