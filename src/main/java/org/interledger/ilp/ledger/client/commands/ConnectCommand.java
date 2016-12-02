package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.interledger.ilp.ledger.client.LedgerCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConnectCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(ConnectCommand.class);
  
  @Override
  protected String getCommand() {
    return "connect";
  }

  @Override
  protected String getDescription() {
    return "Connect the client";
  }
  
  @Override
  protected Options getOptions() {
    return getDefaultOptions();
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    
    if(this.ledgerClient.isConnected()) {
      log.info("Already connected");
      log.debug(this.ledgerClient.getLedgerInfo().toString());
      return;
    }
    
    log.info("Connecting...");
    this.ledgerClient.connect();
    
    log.debug(this.ledgerClient.getLedgerInfo().toString());
    
    log.info("Connected");
    
  }
}
