package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SubscribeToLedgerNotificationCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(SubscribeToLedgerNotificationCommand.class);

  @Override
  public String getCommand() {
    return "subscribeToLedgerNotification";
  }

  @Override
  public String getDescription() {
    return "subscribes to account notifications.";
  }
  
  @Override
  public Options getOptions() {
    return getDefaultOptions()
        .addOption(
            Option.builder("ws").argName("websocket URL").hasArg()
            .desc("The ws: URL of the notification service.").build())
        .addOption(
            Option.builder("account").argName("account").hasArg().required()
        .desc("Account notifications relate to.").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    log.debug("Getting listener service.");
    LedgerAccountService service = ledgerClient.getAccountService();
    
    log.debug("Subscribing to account notification...");
    service.subscribeToAccountNotifications(cmd.getOptionValue("account"));
  }
}
