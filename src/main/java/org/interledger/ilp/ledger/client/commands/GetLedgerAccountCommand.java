package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GetLedgerAccountCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(GetLedgerAccountCommand.class);

  @Override
  public String getCommand() {
    return "getLedgerAccount";
  }

  @Override
  public String getDescription() {
    return "Get account info.";
  }

  @Override
  public Options getOptions() {
    return getDefaultOptions().addOption(Option.builder("account").argName("account").hasArg()
        .desc("Account to query").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    try {
      
      String account = cmd.getOptionValue("account");
      if(account == null) {
        account = this.ledgerClient.getAccount();
      }
      
      log.debug("Getting account details for " + cmd.getOptionValue("account"));
      Account accountData = ledgerClient.getAdaptor().getAccount(cmd.getOptionValue("account"));
      
      log.info(accountData.toString());
    } catch (Exception e) {
      log.error("Error getting account data.", e);
    }
  }
}
