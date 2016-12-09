package org.interledger.ilp.ledger.client.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.LedgerAdaptor;
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
          m.setLedger(cmd.getOptionValue("destination"));
          m.setFrom(cmd.getOptionValue("from"));
          m.setTo(cmd.getOptionValue("to"));
          //FIXME: the ledger message wants data to be an *object* in the json sense of the word, 
          //we use a map for convenience
          Map<String, String> payload = new HashMap<>();
          payload.put("message", cmd.getOptionValue("data"));
          m.setData(payload);
          
      ledgerClient.sendMessage(m);
    } catch (Exception e) {
      log.error("error sending message", e);
    }
  }
}
