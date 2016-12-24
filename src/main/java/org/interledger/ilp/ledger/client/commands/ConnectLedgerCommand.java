package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConnectLedgerCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(ConnectLedgerCommand.class);
  
  @Override
  public String getCommand() {
    return "connectLedger";
  }

  @Override
  public String getDescription() {
    return "Connect the adaptor";
  }
  
  @Override
  public Options getOptions() {
    return getDefaultOptions();
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    
    if(this.ledgerClient.isConnected()) {
      log.info("Already connected");
      log.debug(this.ledgerClient.getAdaptor().getLedgerInfo().toString());
      return;
    }
    
    log.info("Connecting...");
    this.ledgerClient.connect();
    
    log.debug(this.ledgerClient.getAdaptor().getLedgerInfo().toString());
    
    log.info("Connected");
    
  }
}
