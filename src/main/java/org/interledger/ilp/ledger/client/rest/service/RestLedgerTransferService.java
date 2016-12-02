package org.interledger.ilp.ledger.client.rest.service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;

import org.interledger.cryptoconditions.uri.ConditionWriter;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.rest.RestLedgerClient;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerTransfer;
import org.interledger.ilp.ledger.client.rest.json.JsonTransferAccountEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public class RestLedgerTransferService extends RestServiceBase implements LedgerTransferService {

  public RestLedgerTransferService(RestTemplate restTemplate, Map<String, URI> urls) {
    super(restTemplate, urls);
  }

  public static final String TRANSFER_URL_NAME = "transfer";
  public static final String REJECT_TRANSFER_URL_NAME = "transfer_rejection";

  private static final Logger log = LoggerFactory.getLogger(RestLedgerTransferService.class);

  @Override
  public void send(LedgerTransfer transfer) throws Exception {
    try {
      JsonLedgerTransfer nativeTransfer = buildNativeTransfer(transfer);

      log.debug("PUT Transfer - id : {}", nativeTransfer.getId());

      /*
       * regrettably, the spring rest template doesn't support responses for PUTs, which is how
       * transfers are done. we have to do it the hard way
       */
      RequestEntity<JsonLedgerTransfer> request = new RequestEntity<>(nativeTransfer,
          HttpMethod.PUT, URI.create(getServiceUrl(TRANSFER_URL_NAME).replace("{id}", transfer.getId())));

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

      restTemplate.exchange(URI.create(getServiceUrl(REJECT_TRANSFER_URL_NAME).replace("{id}", transfer.getId())), HttpMethod.PUT,
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
  
  
  protected JsonLedgerTransfer buildNativeTransfer(LedgerTransfer transfer) throws IOException {
    
    JsonLedgerTransfer nativeTransfer = new JsonLedgerTransfer();
    nativeTransfer.setId(this.getServiceUrl(TRANSFER_URL_NAME).replace("{id}", transfer.getId()));

    JsonTransferAccountEntry debitAccount = new JsonTransferAccountEntry();

    URI debitAccountIdentifier = URI.create(this.getServiceUrl(RestLedgerAccountService.ACCOUNT_URL_NAME).replace("{name}", transfer.getFromAccount()));
    debitAccount.setAccount(debitAccountIdentifier);
    debitAccount.setAmount(transfer.getAmount());

    debitAccount.setAuthorized(true);
    debitAccount.setMemo(transfer.getNoteToSelf());

    nativeTransfer.setDebits(Arrays.asList(debitAccount));

    JsonTransferAccountEntry creditAccount = new JsonTransferAccountEntry();
    URI creditAccountIdentifier = URI.create(this.getServiceUrl(RestLedgerAccountService.ACCOUNT_URL_NAME).replace("{name}", transfer.getToAccount()));
    creditAccount.setAccount(creditAccountIdentifier);
    creditAccount.setAmount(transfer.getAmount());
    creditAccount.setMemo(transfer.getData());
    
    nativeTransfer.setCredits(Arrays.asList(creditAccount));
    
    nativeTransfer.setLedger(getServiceUrl(RestLedgerClient.LEDGER_URL_NAME));

    if (transfer.getHeader() != null) {
      nativeTransfer.setExpiresAt(transfer.getHeader().getExpiry());

      if (transfer.getHeader().getCondition() != null) {
        try (StringWriter stringWriter = new StringWriter();
            ConditionWriter conditionWriter = new ConditionWriter(stringWriter)) {
          conditionWriter.writeCondition(transfer.getHeader().getCondition());
          nativeTransfer.setExecutionCondition(stringWriter.toString());
        }
      }
    }

    return nativeTransfer;
  }


}
