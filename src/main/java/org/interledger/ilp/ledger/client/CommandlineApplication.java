package org.interledger.ilp.ledger.client;

import java.util.UUID;

import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.model.LedgerInfo;
import org.interledger.ilp.core.ledger.model.TransferRejectedReason;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.core.ledger.service.LedgerMessageService;
import org.interledger.ilp.core.ledger.service.LedgerMetaService;
import org.interledger.ilp.core.ledger.service.LedgerServiceFactory;
import org.interledger.ilp.core.ledger.service.LedgerTransferRejectionService;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.exceptions.RestServiceException;
import org.interledger.ilp.ledger.model.impl.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@SpringBootApplication
public class CommandlineApplication implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(CommandlineApplication.class);

  private LedgerServiceFactory ledgerServiceFactory;

  @Value("${ledger.default.account:issuer}")
  private String defaultAccount;

  @Value("${ledger.rest.username:NULL}")
  private String ledgerUsername;

  @Value("${ledger.rest.password:NULL}")
  private String ledgerPassword;

  public static void main(String[] args) {
    SpringApplication.run("classpath:/META-INF/application-context.xml", args);
  }

  @Override
  public void run(String... args) throws Exception {

    try {
      LedgerMetaService metaService = ledgerServiceFactory.getMetaService();
      LedgerInfo ledgerInfo = metaService.getLedgerInfo();
      log.info(ledgerInfo.toString());
    } catch (RestServiceException e) {
      log.error("Error getting ledger meta data.", e);
    }

    try {
      LedgerAccountService accountService = ledgerServiceFactory.getAccountService();
      Account account = accountService.getAccount(defaultAccount);
      log.info(account.toString());
    } catch (RestServiceException e) {
      log.error("Error getting account data.", e);
    }

    UUID transferId = UUID.randomUUID();
    try {
      LedgerTransferService transferService = ledgerServiceFactory.getTransferService();
      Transfer transfer = new Transfer();
      transfer.setId(transferId.toString());
      transfer.setAmount("100");
      transfer.setFromAccount("admin");
      transfer.setToAccount("hold");

      transferService.send(transfer);
    } catch (Exception e) {
      log.error("Error creating transfer.", e);
    }
    
    try {
      LedgerTransferRejectionService rejectionService = ledgerServiceFactory.getTransferRejectionService();
      Transfer transfer = new Transfer();
      transfer.setId(transferId.toString());
      rejectionService.rejectTransfer(transfer, TransferRejectedReason.TIMEOUT);
    } catch (Exception e) {
      log.error("Error rejecting transfer.", e);
    }
    
    try {
      LedgerMessageService messageService = ledgerServiceFactory.getMessageService();
      messageService.connect();
      log.info("Connected to " + messageService.getConnectionString());
    } catch (RestServiceException e) {
      log.error("Error getting account data.", e);
    }
    
    System.out.println("\n\n\nPress any key to exit\n\n\n");
    System.in.read();    
  }

  @Bean
  public UsernamePasswordAuthenticationToken getAuthToken() {
    if (!"NULL".equals(ledgerUsername) && !"NULL".equals(ledgerPassword)) {
      return new UsernamePasswordAuthenticationToken(ledgerUsername, ledgerPassword);
    }
    return null;
  }

  @Autowired
  public void setLedgerServiceFactory(LedgerServiceFactory factory) {
    this.ledgerServiceFactory = factory;
  }

}
