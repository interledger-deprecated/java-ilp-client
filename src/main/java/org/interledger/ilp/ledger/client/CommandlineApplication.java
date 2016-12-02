package org.interledger.ilp.ledger.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.cli.HelpFormatter;
import org.interledger.ilp.ledger.client.events.LedgerNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@SpringBootApplication
public class CommandlineApplication implements CommandLineRunner, ApplicationContextAware{

  private static final Logger log = LoggerFactory.getLogger(CommandlineApplication.class);

  private ApplicationContext applicationContext;
  
  public static void main(String[] args) {
    SpringApplication.run("classpath:/META-INF/application-context.xml", args);
  }

  public void run(String... args) throws Exception {
    
    if(args.length == 0) {
      
      Map<String, LedgerCommand> commands = applicationContext.getBeansOfType(LedgerCommand.class);

      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("<command> [options]", commands.get("helpCommand").getDefaultOptions());
      
       System.out.println("\r\n"
          + "Settings are read from application.properties but may be overridden through options.\r\n"
          + "\r\n"
          + "Commands:\r\n");
       
       for (LedgerCommand command : commands.values()) {
         System.out.println(command.getCommand() + " - " + command.getDescription());
       }
       
      //Loop and read in commands
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String commandLine = reader.readLine();
      
      while(!"quit".equals(commandLine.trim())) {
        
      args = commandLine.split(" ");
        
        if(args.length > 0) {
          LedgerCommand command = LedgerCommand.getLedgerCommand(args[0], commands);
          if(command != null) {
            try {
              command.run(args);
            } catch (Exception e) {
              e.printStackTrace(System.err);
            }
          } else {
            log.error("Unrecognized command: " + args[0]);
          }
        } else {
          //Empty line
        }
        
        commandLine = reader.readLine();
        
      }
    }
    /*
    try {
      LedgerMetaService metaService = ledgerClient.getMetaService();
      LedgerInfo ledgerInfo = metaService.getLedgerInfo();
      log.info(ledgerInfo.toString());
    } catch (RestServiceException e) {
      log.error("Error getting ledger meta data.", e);
    }

 
    try {
      LedgerTransferRejectionService rejectionService = ledgerClient.getTransferRejectionService();
      Transfer transfer = new Transfer();
      transfer.setId(transferId.toString());
      rejectionService.rejectTransfer(transfer, TransferRejectedReason.TIMEOUT);
    } catch (Exception e) {
      log.error("Error rejecting transfer.", e);
    }
    
    try {
      LedgerNotificationListenerService messageService = ledgerClient.getMessageService();
      messageService.connect();
      log.info("Connected to " + messageService.getConnectionString());
    } catch (RestServiceException e) {
      log.error("Error getting account data.", e);
    }
    
    System.out.println("\n\n\nPress any key to exit\n\n\n");
    System.in.read();    
    */
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @EventListener
  public void handleTransferNotification(LedgerNotificationEvent event) {
    log.info("received ledger notification event {}", event);
  }
  
  
}
