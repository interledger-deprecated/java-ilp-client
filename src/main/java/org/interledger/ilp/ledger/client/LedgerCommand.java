package org.interledger.ilp.ledger.client;



import java.util.Arrays;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.interledger.ilp.core.ledger.LedgerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public abstract class LedgerCommand implements CommandLineRunner {

  protected CommandLineParser parser = new DefaultParser();
  protected HelpFormatter formatter = new HelpFormatter();
  protected LedgerClient ledgerClient;

  @Value("${ledger.rest.username:NULL}")
  private String ledgerUsername;

  @Value("${ledger.rest.password:NULL}")
  private String ledgerPassword;

  @Bean
  public UsernamePasswordAuthenticationToken getAuthToken() {
    if (!"NULL".equals(ledgerUsername) && !"NULL".equals(ledgerPassword)) {
      return new UsernamePasswordAuthenticationToken(ledgerUsername, ledgerPassword);
    }
    return null;
  }

  @Autowired
  public void setLedgerServiceFactory(LedgerClient client) {
    this.ledgerClient = client;
  }

  public void run(String... args) throws Exception {
    if (args.length > 0 && args[0].equals(getCommand())) {
      try {
        CommandLine cmd = parser.parse(getOptions(), Arrays.copyOfRange(args, 1, args.length));
        runCommand(cmd);
      } catch (UnrecognizedOptionException | MissingArgumentException e) {
        System.err.println("Error parsing options.");
        printHelp();
      }
    }
  }

  protected abstract String getCommand();

  protected abstract String getDescription();

  public Options getDefaultOptions() {
    return new Options()
        .addOption(
            Option.builder("u").argName("user").hasArg()
            .desc("Username to use for ledger authentication").build())
        .addOption(
            Option.builder("p").argName("password").hasArg()
            .desc("Password to use for ledger authentication").build());
  }
  
  protected abstract Options getOptions();

  protected abstract void runCommand(CommandLine cmd) throws Exception;

  public final void printHelp() {
    formatter.printHelp(getCommand() + " [options]", getDescription(), getOptions(),
        getFooter());
  }

  public static LedgerCommand getLedgerCommand(String commandName,
      Map<String, LedgerCommand> commands) {
    String beanName = commandName + "Command";
    if (commands.containsKey(beanName)) {
      return commands.get(beanName);
    }
    return null;
  }

  private String getFooter() {
    // TODO Auto-generated method stub
    return "\r\n\r\n";
  }

}
