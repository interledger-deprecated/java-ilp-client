package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
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
    return getDefaultOptions().addOption(Option.builder("a").argName("account").hasArg().required()
        .desc("Account to query").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    try {
      log.debug("Getting account service");
      LedgerAccountService accountService = ledgerClient.getAccountService();
      
      log.debug("Getting account details for " + cmd.getOptionValue("a"));
      Account account = accountService.getAccount(cmd.getOptionValue("a"));
      
      log.info(account.toString());
    } catch (Exception e) {
      log.error("Error getting account data.", e);
    }
  }
}
