package org.interledger.ilp.ledger.client.rest.service;

import java.net.URI;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.core.ledger.service.LedgerTransferRejectionService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class RestLedgerTransferRejectionService extends RestServiceBase
    implements LedgerTransferRejectionService {

  @Override
  public void rejectTransfer(LedgerTransfer transfer, TransferRejectedReason reason)
      throws Exception {
    log.debug("Rejecting Transfer - id : {}", transfer.getId());

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.TEXT_PLAIN);

      HttpEntity<Object> rejectionRequest = new HttpEntity<>(reason.toString(), headers);

      restTemplate.exchange(URI.create(serviceUrl.replace(":id", transfer.getId())), HttpMethod.PUT,
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
  public void setServiceUrl(String serviceUrl) {
    // we dont want to use the underlying url fix right now.
    this.serviceUrl = serviceUrl;
  }
}
