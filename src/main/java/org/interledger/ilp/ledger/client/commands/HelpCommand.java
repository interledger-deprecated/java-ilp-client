package org.interledger.ilp.ledger.client.commands;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand extends LedgerCommand implements ApplicationContextAware{

  private static final Logger log = LoggerFactory.getLogger(HelpCommand.class);

  private ApplicationContext applicationContext;

  @Override
  public String getCommand() {
    return "help";
  }

  @Override
  public String getDescription() {
    return "Get help on a command";
  }

  @Override
  public Options getOptions() {
    return new Options().addOption(Option.builder("c").argName("command").hasArg()
        .desc("Name of the command.").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    
    String commandName = cmd.getOptionValue("c");
    
    if(commandName != null) {
      Map<String, LedgerCommand> commands = applicationContext.getBeansOfType(LedgerCommand.class);    
      LedgerCommand command = LedgerCommand.getLedgerCommand(commandName, commands);
      
      if(command != null) {
        command.printHelp();
      } else {
        log.error("Unknown command: " + commandName);
      }
      
    } else {
      printHelp();
    }

  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
