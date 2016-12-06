package org.interledger.ilp.ledger.client.rest.services;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.UUID;

import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.ledger.adaptor.rest.service.RestLedgerTransferService;
import org.interledger.ilp.ledger.client.model.ClientLedgerTransfer;
import org.interledger.ilp.ledger.client.rest.MockRestClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class RemoteLedgerTransferRejectionServiceTests {

  private MockRestServiceServer mockServer;
  private MockRestClient mockClient;
  
  private RestTemplate restTemplate;

  @Before
  public void setup() {
    this.restTemplate = new RestTemplate();
    this.mockServer =
        MockRestServiceServer.bindTo(this.restTemplate).ignoreExpectOrder(true).build();
    this.mockClient = new MockRestClient(null, "/");
  }

  @Test
  public void sendLedgerRejectionSuccess() throws Exception {
    UUID transferId = UUID.randomUUID();

    this.mockServer.expect(requestTo("/transfers/" + transferId.toString() + "/rejection"))
        .andExpect(method(HttpMethod.PUT))
        .andExpect(content().contentType(MediaType.TEXT_PLAIN))
        .andExpect(content().string("REJECTED_BY_RECEIVER"))
        .andRespond(withSuccess("REJECTED_BY_RECEIVER", MediaType.TEXT_PLAIN));
    
    RestLedgerTransferService service = new RestLedgerTransferService(mockClient, restTemplate);
    
    ClientLedgerTransfer transfer = new ClientLedgerTransfer();
    transfer.setId(transferId.toString());
    
    service.rejectTransfer(transfer, TransferRejectedReason.REJECTED_BY_RECEIVER);

    this.mockServer.verify();
  }

}
