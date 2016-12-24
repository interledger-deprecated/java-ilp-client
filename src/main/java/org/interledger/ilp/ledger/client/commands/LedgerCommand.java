package org.interledger.ilp.ledger.client.commands;

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
import org.interledger.ilp.ledger.client.LedgerClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public abstract class LedgerCommand implements CommandLineRunner, ApplicationContextAware {

  protected CommandLineParser parser = new DefaultParser();
  protected HelpFormatter formatter = new HelpFormatter();
  protected LedgerClient ledgerClient;
  
  //TODO Get adaptor bean by name based on parameter
  private String ledgerAdaptortName;

  @Value("${ledger.rest.username:NULL}")
  private String ledgerUsername;

  @Value("${ledger.rest.password:NULL}")
  private String ledgerPassword;
  
  protected ApplicationContext applicationContext;

  @Bean
  public UsernamePasswordAuthenticationToken getAuthToken() {
    if (!"NULL".equals(ledgerUsername) && !"NULL".equals(ledgerPassword)) {
      return new UsernamePasswordAuthenticationToken(ledgerUsername, ledgerPassword);
    }
    return null;
  }

  @Autowired
  public void setLedgerClient(LedgerClient client) {
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

  public abstract String getCommand();

  public abstract String getDescription();

  public Options getDefaultOptions() {
    return new Options()
        .addOption(Option.builder("l").argName("ledger").hasArg()
            .desc("Ledger adaptor bean name." + (ledgerAdaptortName != null ? " (" + ledgerAdaptortName + ")" : "")).build())
        .addOption(Option.builder("u").argName("user").hasArg()
            .desc("Username to use for ledger authentication." + (ledgerUsername != null ? " (" + ledgerUsername + ")" : "")).build())
        .addOption(Option.builder("p").argName("password").hasArg()
            .desc("Password to use for ledger authentication" + (ledgerPassword != null ? " (" + ledgerPassword + ")" : "")).build());
  }

  public abstract Options getOptions();

  protected abstract void runCommand(CommandLine cmd) throws Exception;


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
  
  public final void printHelp() {
    formatter.printHelp(getCommand() + " [options]", getDescription(), getOptions(), getFooter());
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
