package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.service.LedgerNotificationListenerService;
import org.interledger.ilp.ledger.client.LedgerCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ListenCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(ListenCommand.class);

  @Override
  protected String getCommand() {
    return "listen";
  }

  @Override
  protected String getDescription() {
    return "Listen on websocket for notifications.";
  }
  
  @Override
  protected Options getOptions() {
    return new Options().addOption(Option.builder("ws").argName("websocket URL").hasArg()
        .desc("The ws: URL of the notification service.").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    
    log.debug("Getting listener service.");
    LedgerNotificationListenerService service = ledgerClient.getLedgerNotificationListenerService();
    
    log.debug("Connecting listener...");
    service.connect();
    
    System.out.println("\n\n\nPress any key to disconnect\n\n\n");
    System.in.read();
    
    log.debug("Disconnecting listener...");
    service.disconnect();
    log.debug("Disconnected");
  }
}
