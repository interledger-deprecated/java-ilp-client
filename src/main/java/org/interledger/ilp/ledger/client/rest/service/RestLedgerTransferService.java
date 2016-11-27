package org.interledger.ilp.ledger.client.rest.service;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.Arrays;

import org.interledger.cryptoconditions.uri.ConditionWriter;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerTransfer;
import org.interledger.ilp.ledger.client.rest.json.JsonTransferAccountEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class RestLedgerTransferService extends RestServiceBase implements LedgerTransferService {

  private static final Logger log = LoggerFactory.getLogger(RestLedgerTransferService.class);

  protected String accountUrl;
  protected String ledgerUrl;

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
          HttpMethod.PUT, URI.create(serviceUrl.replace(":id", transfer.getId())));

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
  public void setServiceUrl(String serviceUrl) {
    //we dont want to use the underlying url fix, right now.
    this.serviceUrl = serviceUrl;
  }


  public void setAccountUrl(String accountUrl) {
    this.accountUrl = accountUrl;
  }

  public String getAccountUrl() {
    return this.accountUrl;
  }

  public void setLedgerUrl(String ledgerUrl) {
    this.ledgerUrl = ledgerUrl;
  }

  public String getLedgerUrl() {
    return this.ledgerUrl;
  }

  protected JsonLedgerTransfer buildNativeTransfer(LedgerTransfer transfer) throws IOException {
    JsonLedgerTransfer nativeTransfer = new JsonLedgerTransfer();
    nativeTransfer.setId(this.serviceUrl.replace(":id", transfer.getId()));

    JsonTransferAccountEntry debitAccount = new JsonTransferAccountEntry();

    debitAccount.setAccount(URI.create(accountUrl.replace(":name", transfer.getFromAccount())));
    debitAccount.setAmount(transfer.getAmount());

    debitAccount.setAuthorized(true);
    debitAccount.setMemo(transfer.getNoteToSelf());

    nativeTransfer.setDebits(Arrays.asList(debitAccount));

    JsonTransferAccountEntry creditAccount = new JsonTransferAccountEntry();
    creditAccount.setAccount(URI.create(accountUrl.replace(":name", transfer.getToAccount())));
    creditAccount.setAmount(transfer.getAmount());

    creditAccount.setMemo(transfer.getData());
    nativeTransfer.setCredits(Arrays.asList(creditAccount));

    nativeTransfer.setLedger(ledgerUrl);

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
