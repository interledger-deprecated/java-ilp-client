package org.interledger.ilp.ledger.client.commands;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.ledger.LedgerAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AdaptorsCommand extends LedgerCommand implements ApplicationContextAware{

  private static final Logger log = LoggerFactory.getLogger(AdaptorsCommand.class);

  private ApplicationContext applicationContext;

  @Override
  public String getCommand() {
    return "adaptors";
  }

  @Override
  public String getDescription() {
    return "List or select available adaptors";
  }

  @Override
  public Options getOptions() {
    return new Options().addOption(Option.builder("load").argName("load").hasArg()
        .desc("Adaptor to load.").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    String adaptorName = cmd.getOptionValue("load");
    Map<String, LedgerAdaptor> adaptors = applicationContext.getBeansOfType(LedgerAdaptor.class);
    
    if(adaptorName != null){
      log.debug("Loading adaptor: " + adaptorName);
      LedgerAdaptor adaptor = adaptors.get(adaptorName);
      if(adaptor != null) {
        //TODO Set adaptor
        throw new RuntimeException("Not implemented yet");
      } else {
        log.debug("Adaptor not found: " + adaptorName);
      }
    } else {
      System.out.println("\r\nAvailable adaptors:\r\n");
      for (Entry<String, LedgerAdaptor> adaptor : adaptors.entrySet()) {
        System.out.println("> " + adaptor.getKey() + " - " + adaptor.getValue().getClass().getSimpleName());
      }
      
    }
    
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

}
