package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;
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
            Option.builder("from").argName("from account").hasArg()
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
      
      LedgerTransfer transfer = buildTransfer(cmd);
      ledgerClient.getAdaptor().sendTransfer(transfer);
      
    } catch (Exception e) {
      
      log.error("Error creating transfer.", e);
      
    }

  }

  private LedgerTransfer buildTransfer(CommandLine cmd) throws Exception {
    ClientLedgerTransfer transfer = new ClientLedgerTransfer();
    
    transfer.setAmount(cmd.getOptionValue("amount"));
    transfer.setFromAccount(cmd.getOptionValue("from") == null ? this.ledgerClient.getAccount() : cmd.getOptionValue("from"));
    transfer.setToAccount(cmd.getOptionValue("to"));
    transfer.setAuthorized(true);
    
    return transfer;
  }
}
