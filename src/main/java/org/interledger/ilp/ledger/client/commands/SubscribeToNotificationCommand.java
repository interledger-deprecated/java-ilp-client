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
public class SubscribeToNotificationCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(SubscribeToNotificationCommand.class);

  @Override
  protected String getCommand() {
    return "subscribeToNotification";
  }

  @Override
  protected String getDescription() {
    return "subscribes to account notifications.";
  }
  
  @Override
  protected Options getOptions() {
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
    LedgerNotificationListenerService service = ledgerClient.getLedgerNotificationListenerService();
    
    log.debug("Subscribing to account notification...");
    service.subscribeToAccountNotifications(cmd.getOptionValue("account"));
  }
}
