package org.interledger.ilp.ledger.adaptor.rest.service;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.adaptor.rest.RestLedgerAdaptor;
import org.interledger.ilp.ledger.adaptor.rest.ServiceUrls;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransferAccountEntry;
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
  public void sendTransfer(LedgerTransfer transfer) throws Exception {
    try {
      
      JsonLedgerTransfer jsonTransfer = buildJsonTransfer(transfer);
      log.debug("PUT Transfer - id : {}", jsonTransfer.getId());
            
      //FIXME - This is ugly. We use the transfer's ID as the URI instead of adding the ID to the endpoint URL
      //URI uri = new UriTemplate(getServiceUrl(TRANSFER_URL_NAME)).expand(transfer.getId());
      URI uri = URI.create(transfer.getId());
      RequestEntity<JsonLedgerTransfer> request = RequestEntity.put(uri)
          .contentType(MediaType.APPLICATION_JSON_UTF8).body(jsonTransfer, JsonLedgerTransfer.class);
      ResponseEntity<JsonLedgerTransfer> rsp = restTemplate.exchange(request,
          JsonLedgerTransfer.class);

      log.debug("Transfer Response: ", rsp.getBody());

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
  public void rejectTransfer(LedgerTransfer transfer, TransferRejectedReason reason)
      throws Exception {
    
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
  public URI getNextTransferId() {
    //FIXME - This is ugly. It should return just the UUID.
    return new UriTemplate(getServiceUrl(ServiceUrls.TRANSFER)).expand(UUID.randomUUID());
  }
  
  protected JsonLedgerTransfer buildJsonTransfer(LedgerTransfer transfer) {
    
    //TODO Validate that ids are URIs before we try to convert them
    
    JsonLedgerTransfer jsonTransfer = new JsonLedgerTransfer();
    jsonTransfer.setId(URI.create(transfer.getId()));
    
    //FIXME Should we get this from the adaptor or at least compare with the adaptor?
    jsonTransfer.setLedgerId(URI.create(transfer.getLedgerId()));

    List<JsonLedgerTransferAccountEntry> credits = new LinkedList<>();
    JsonLedgerTransferAccountEntry jsonCreditEntry = new JsonLedgerTransferAccountEntry();
    jsonCreditEntry.setAccount(URI.create(transfer.getToAccount()));
    jsonCreditEntry.setAmount(transfer.getAmount());
    jsonCreditEntry.setMemo(transfer.getMemo());
    credits.add(jsonCreditEntry);
    jsonTransfer.setCredits(credits);
    
    List<JsonLedgerTransferAccountEntry> debits = new LinkedList<>();
    JsonLedgerTransferAccountEntry jsonDebitEntry = new JsonLedgerTransferAccountEntry();
    jsonDebitEntry.setAccount(URI.create(transfer.getFromAccount()));
    jsonDebitEntry.setAmount(transfer.getAmount());
    jsonDebitEntry.setMemo(transfer.getMemo());
    jsonDebitEntry.setAuthorized(true);
    debits.add(jsonDebitEntry);
    jsonTransfer.setDebits(debits);
    
    jsonTransfer.setCancellationCondition(transfer.getCancellationCondition());
    jsonTransfer.setExecutionCondition(transfer.getExecutionCondition());
    jsonTransfer.setExpiresAt(transfer.getExpiresAt());

    return jsonTransfer;
    
  }

}
