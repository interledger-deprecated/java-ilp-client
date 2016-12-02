package org.interledger.ilp.ledger.client.commands;

import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.service.LedgerTransferService;
import org.interledger.ilp.ledger.client.LedgerCommand;
import org.interledger.ilp.ledger.model.impl.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransferCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(TransferCommand.class);

  @Override
  protected String getCommand() {
    return "transfer";
  }

  @Override
  protected String getDescription() {
    return "Transfer from one account to another.";
  }
  
  @Override
  protected Options getOptions() {
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
      
      Transfer transfer = new Transfer();
      
      UUID transferId = UUID.randomUUID();
      transfer.setId(transferId.toString());
      
      transfer.setAmount(cmd.getOptionValue("amount"));
      transfer.setFromAccount(cmd.getOptionValue("from"));
      transfer.setToAccount(cmd.getOptionValue("to"));

      transferService.send(transfer);
      
    } catch (Exception e) {
      
      log.error("Error creating transfer.", e);
      
    }

  }
}
