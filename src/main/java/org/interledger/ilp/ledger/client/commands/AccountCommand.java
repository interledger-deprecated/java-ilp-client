package org.interledger.ilp.ledger.client.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.model.Account;
import org.interledger.ilp.core.ledger.service.LedgerAccountService;
import org.interledger.ilp.ledger.client.LedgerCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountCommand extends LedgerCommand {

  private static final Logger log = LoggerFactory.getLogger(AccountCommand.class);

  @Override
  protected String getCommand() {
    return "account";
  }

  @Override
  protected String getDescription() {
    return "Get account info.";
  }

  @Override
  protected Options getOptions() {
    return getDefaultOptions().addOption(Option.builder("a").argName("account").hasArg().required()
        .desc("Account to query").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    try {
      log.debug("Getting account service");
      LedgerAccountService accountService = ledgerClient.getAccountService();
      
      log.debug("Getting account details for " + cmd.getOptionValue("from"));
      Account account = accountService.getAccount(cmd.getOptionValue("from"));
      
      log.info(account.toString());
    } catch (Exception e) {
      log.error("Error getting account data.", e);
    }
  }
}
