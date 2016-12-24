package org.interledger.ilp.ledger.adaptor.rest.service;

import java.net.URI;
import java.util.UUID;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

public class RestLedgerTransferService extends RestServiceBase implements LedgerTransferService {

  public RestLedgerTransferService(RestLedgerAdaptor adaptor, RestTemplate restTemplate) {
    super(adaptor, restTemplate);
  }

  private static final Logger log = LoggerFactory.getLogger(RestLedgerTransferService.class);

  @Override
  public void sendTransfer(LedgerTransfer transfer) {
    try {
      
      JsonLedgerTransfer jsonTransfer = JsonLedgerTransfer.fromLedgerTransfer(transfer, adaptor);
      
      if(!jsonTransfer.getLedger().toString().equals(getServiceUrl(ServiceUrls.LEDGER))) {
        throw new IllegalArgumentException("Can't make transfers on other ledgers. Illegal ledger identifier: " + jsonTransfer.getLedger());
      }
      
      if(!jsonTransfer.getId().toString().startsWith(getServiceUrl(ServiceUrls.LEDGER))) {
        throw new IllegalArgumentException("Illegal transfer identifier: " + transfer.getId());
      }
      
      log.debug("PUT Transfer - id : {}", jsonTransfer.getId());
      
      
      RequestEntity<JsonLedgerTransfer> request = RequestEntity.put(jsonTransfer.getId())
          .contentType(MediaType.APPLICATION_JSON_UTF8).body(jsonTransfer, JsonLedgerTransfer.class);
      ResponseEntity<JsonLedgerTransfer> rsp = restTemplate.exchange(request,
          JsonLedgerTransfer.class);

      log.trace("Transfer Response: " + rsp.getBody());

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
  public void rejectTransfer(LedgerTransfer transfer, TransferRejectedReason reason) {
    
    log.debug("Rejecting Transfer - id : {}", transfer.getId());

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.TEXT_PLAIN);

      HttpEntity<Object> rejectionRequest = new HttpEntity<>(reason.toString(), headers);

      restTemplate.exchange(URI.create(getServiceUrl(ServiceUrls.TRANSFER).replace("{id}", transfer.getId())), HttpMethod.PUT,
          rejectionRequest, String.class);

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
  public String getNextTransferId() {
    return new UriTemplate(getServiceUrl(ServiceUrls.TRANSFER)).expand(UUID.randomUUID()).toString();
  }
  
}
