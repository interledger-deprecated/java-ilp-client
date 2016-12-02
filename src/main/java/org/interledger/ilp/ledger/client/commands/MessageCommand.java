package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.ledger.client.LedgerCommand;
import org.springframework.stereotype.Component;

@Component
public class MessageCommand extends LedgerCommand {

  @Override
  protected String getCommand() {
    return "message";
  }

  @Override
  protected String getDescription() {
    return "Send a message.";
  }
  
  @Override
  protected Options getOptions() {
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
    throw new Exception("Not implemented");
  }
}
