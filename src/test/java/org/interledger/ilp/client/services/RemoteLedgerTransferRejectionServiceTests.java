package org.interledger.ilp.client.services;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.UUID;

import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.core.ledger.service.LedgerTransferRejectionService;
import org.interledger.ilp.ledger.client.rest.json.JsonLedgerInfo;
import org.interledger.ilp.ledger.client.rest.service.RestLedgerTransferRejectionService;
import org.interledger.ilp.ledger.model.impl.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.ResourceProperties.Content;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class RemoteLedgerTransferRejectionServiceTests {

  private MockRestServiceServer mockServer;

  private RestTemplate restTemplate;

  @Before
  public void setup() {
    this.restTemplate = new RestTemplate();
    this.mockServer =
        MockRestServiceServer.bindTo(this.restTemplate).ignoreExpectOrder(true).build();
  }

  @Test
  public void sendLedgerRejectionSuccess() throws Exception {
    UUID transferId = UUID.randomUUID();

    this.mockServer.expect(requestTo("/transfers/" + transferId.toString() + "/rejection"))
        .andExpect(method(HttpMethod.PUT))
        .andExpect(content().contentType(MediaType.TEXT_PLAIN))
        .andExpect(content().string("REJECTED_BY_RECEIVER"))
        .andRespond(withSuccess("REJECTED_BY_RECEIVER", MediaType.TEXT_PLAIN));
    
    RestLedgerTransferRejectionService service = new RestLedgerTransferRejectionService();
    service.setServiceUrl("/transfers/:id/rejection");
    service.setRestTemplate(restTemplate);
    
    Transfer transfer = new Transfer();
    transfer.setId(transferId.toString());
    
    service.rejectTransfer(transfer, TransferRejectedReason.REJECTED_BY_RECEIVER);

    this.mockServer.verify();
  }

}
