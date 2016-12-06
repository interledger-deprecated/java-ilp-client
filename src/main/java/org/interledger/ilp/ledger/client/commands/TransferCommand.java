package org.interledger.ilp.ledger.client.commands;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.interledger.ilp.core.ledger.model.LedgerTransferAccountEntry;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.model.ClientLedgerTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransferCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(TransferCommand.class);

  @Override
  public String getCommand() {
    return "transfer";
  }

  @Override
  public String getDescription() {
    return "Transfer from one account to another.";
  }
  
  @Override
  public Options getOptions() {
    return getDefaultOptions()
        .addOption(
            Option.builder("from").argName("from account").hasArg().required()
            .desc("Account to be debited").build())
        .addOption(
            Option.builder("to").argName("to account").hasArg().required()
            .desc("Account to be credited").build())
        .addOption(
            Option.builder("amount").argName("amount").hasArg().required()
            .desc("Amount to be transferred").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    
    try {
      
      LedgerTransferService transferService = ledgerClient.getTransferService();
      LedgerTransfer transfer = buildTransfer(cmd);
      transferService.sendTransfer(transfer);
      
    } catch (Exception e) {
      
      log.error("Error creating transfer.", e);
      
    }

  }

  private LedgerTransfer buildTransfer(CommandLine cmd) throws Exception {
    ClientLedgerTransfer transfer = new ClientLedgerTransfer();
    
    transfer.setId(this.ledgerClient.getTransferService().getNextTransferId().toString());
    transfer.setLedger(this.ledgerClient.getLedgerInfo().getId());
    
    List<LedgerTransferAccountEntry> debits = buildAccountEntries(cmd.getOptionValue("amount"), cmd.getOptionValue("from"));
    List<LedgerTransferAccountEntry> credits = buildAccountEntries(cmd.getOptionValue("amount"), cmd.getOptionValue("to"));
    
    transfer.setDebits(debits);
    transfer.setCredits(credits);
    
    return transfer;
  }

  private List<LedgerTransferAccountEntry> buildAccountEntries(String amount, String account) {
    List<LedgerTransferAccountEntry> debits = new LinkedList<>();
    debits.add(new LedgerTransferAccountEntry() {
      
      @Override
      public Boolean isRejected() {
        return false;
      }
      
      @Override
      public Boolean isAuthorized() {
        return true;
      }
      
      @Override
      public String getRejectionMessage() {
        return null;
      }
      
      @Override
      public Object getMemo() {
        return null;
      }
      
      @Override
      public URI getInvoice() {
        return null;
      }
      
      @Override
      public String getAmount() {
        return amount;
      }
      
      @Override
      public URI getAccount() {
        return URI.create(account);
      }
    });
    return debits;
  }
}
