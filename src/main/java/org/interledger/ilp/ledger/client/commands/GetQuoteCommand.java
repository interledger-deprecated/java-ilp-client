package org.interledger.ilp.ledger.client.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.crypto.CipherInputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.ledger.client.LedgerClient;
import org.interledger.ilp.ledger.client.events.ApplicationEventPublishingLedgerEventHandler;
import org.interledger.ilp.ledger.client.model.ClientQuoteQueryParams;
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
            .desc("Source Ledger").build())
        .addOption(
            Option.builder("destAddress").argName("destination address").hasArg().required()
            .desc("Recipients Ledger").build())
        .addOption(Option.builder("connector").argName("connector").hasArg().required()
            .desc("Connector").build());
    
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    ClientQuoteQueryParams quoteParams = new ClientQuoteQueryParams();
    quoteParams.setSourceAddress(cmd.getOptionValue("sourceAddress"));
    quoteParams.setSourceAmount(cmd.getOptionValue("sourceAmount"));
    quoteParams.setDestinationAmount(cmd.getOptionValue("destAmount"));
    quoteParams.setDestinationAddress(cmd.getOptionValue("destAddress"));

    Set<String> connectors = new HashSet<String>();
    connectors.add(cmd.getOptionValue("connector"));
    quoteParams.setConnectors(connectors);
    
    // FIXME: here as a POC, the Application needs to be refactored to use client, not adaptor
    LedgerClient client = new LedgerClient(ledgerClient, "admin", null,
        new ApplicationEventPublishingLedgerEventHandler(applicationContext));

    client.requestQuote(quoteParams);
  }

}

