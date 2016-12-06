package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.ledger.client.model.ClientLedgerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageCommand extends LedgerCommand {
  private static final Logger log = LoggerFactory.getLogger(MessageCommand.class);

  @Override
  public String getCommand() {
    return "message";
  }

  @Override
  public String getDescription() {
    return "Send a message.";
  }
  
  @Override
  public Options getOptions() {
    return getDefaultOptions()
        .addOption(
            Option.builder("from").argName("from account").hasArg().required()
            .desc("Account from which message is sent.").build())
        .addOption(
            Option.builder("to").argName("to account").hasArg().required()
            .desc("Account to which message is sent.").build())
        .addOption(
            Option.builder("data").argName("data").hasArg().required()
            .desc("Message to be sent.").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    try {
      ClientLedgerMessage m = new ClientLedgerMessage();
          m.setFromAccount(cmd.getOptionValue("from"));
          m.setToAccount(cmd.getOptionValue("to"));
          m.setData(cmd.getOptionValue("data"));
      ledgerClient.sendMessage(m);
    } catch (Exception e) {
      log.error("error sending message", e);
    }
  }
}
