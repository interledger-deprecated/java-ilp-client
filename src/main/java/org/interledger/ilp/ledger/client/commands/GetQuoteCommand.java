package org.interledger.ilp.ledger.client.commands;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.ledger.client.json.JsonQuoteRequest;
import org.springframework.stereotype.Component;

@Component
public class GetQuoteCommand extends LedgerCommand {

  @Override
  public String getCommand() {
    return "getQuote";
  }

  @Override
  public String getDescription() {
    return "Request a quote";
  }

  @Override
  public Options getOptions() {
    return getDefaultOptions()
        .addOption(
            Option.builder("sourceAmount").argName("source amount").hasArg()
            .desc("Source Amount").build())
        .addOption(
            Option.builder("destAmount").argName("destination amount").hasArg()
            .desc("Destination Amount").build())
        .addOption(
            Option.builder("sourceAddress").argName("source address").hasArg()
            .desc("Source ILP Address").build())
        .addOption(
            Option.builder("destAddress").argName("destination address").hasArg().required()
            .desc("Recipients ILP Address").build())
        .addOption(
            Option.builder("expiry").argName("destination expiry").hasArg()
            .desc("Quote expiry").build())
        .addOption(Option.builder("connector").argName("connector").hasArg()
            .desc("Connector").build());
    
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    JsonQuoteRequest quoteParams = new JsonQuoteRequest();
    quoteParams.setSourceAddress(cmd.getOptionValue("sourceAddress"));
    quoteParams.setSourceAmount(cmd.getOptionValue("sourceAmount"));
    quoteParams.setDestinationAmount(cmd.getOptionValue("destAmount"));
    quoteParams.setDestinationAddress(cmd.getOptionValue("destAddress"));
    if(cmd.getOptionValue("destExpiry") != null) {
      quoteParams.setDestinationExpiryDuration(Integer.valueOf(cmd.getOptionValue("destExpiry")));
    }
    
    if(cmd.getOptionValue("connector") != null) {
      Set<String> connectors = new HashSet<String>();
      connectors.add(cmd.getOptionValue("connector"));
      quoteParams.setConnectors(connectors);      
    }
    
    this.ledgerClient.requestQuote(quoteParams);
    
  }

}

