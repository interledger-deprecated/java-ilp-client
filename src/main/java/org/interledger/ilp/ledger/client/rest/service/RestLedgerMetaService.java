package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.service.LedgerMetaService;
import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class RestLedgerMetaService extends RestServiceBase implements LedgerMetaService {

  private LedgerInfo cache;

  private String metadataPath;
  
  public RestLedgerMetaService(@Value("${ledger.rest.metadata_path:}") String metadataPath) {
    this.metadataPath = metadataPath;
  }
  
  @Override
  public LedgerInfo getLedgerInfo() throws HttpStatusCodeException, RestServiceException {

    if (cache == null) {
      try {

        log.debug("GET Meta");

        LedgerInfo ledgerInfo = restTemplate.getForObject(
            URI.create(this.serviceUrl).resolve(metadataPath != null ? metadataPath : ""),
            JsonLedgerInfo.class);

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
