package org.interledger.ilp.ledger.client.commands;

import java.nio.charset.Charset;
import java.util.UUID;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.interledger.ilp.core.client.model.MessageData;
import org.interledger.ilp.ledger.client.json.JsonMessageEnvelope;
import org.interledger.ilp.ledger.client.model.ClientLedgerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Component
public class MessageCommand extends LedgerCommand {
  private static final Logger log = LoggerFactory.getLogger(MessageCommand.class);

  @Override
  public String getCommand() {
    return "message";
  }

  @Override
  public String getDescription() {
    return "Send a message.";
  }

  @Override
  public Options getOptions() {
    return getDefaultOptions()
        .addOption(Option.builder("from").argName("from account").hasArg().required()
            .desc("Account from which message is sent.").build())
        .addOption(Option.builder("to").argName("to account").hasArg().required()
            .desc("Account to which message is sent.").build())
        .addOption(Option.builder("data").argName("data").hasArg().required()
            .desc("Message to be sent.").build());
  }

  @Override
  protected void runCommand(CommandLine cmd) throws Exception {
    try {
      
      GenericMessageEnvelope innerMessage = new GenericMessageEnvelope();
      innerMessage.setId(UUID.randomUUID().toString());
      innerMessage.setData(new GenericMessageData(cmd.getOptionValue("data")));
      
      ObjectMapper mapper = new ObjectMapper();
      String messageData = mapper.writeValueAsString(innerMessage);
      
      ClientLedgerMessage m = new ClientLedgerMessage();
      m.setLedger(cmd.getOptionValue("destination"));
      m.setFrom(cmd.getOptionValue("from"));
      m.setTo(cmd.getOptionValue("to"));
      m.setData(messageData.getBytes(Charset.forName("UTF-8")));

      ledgerClient.getAdaptor().sendMessage(m);
    } catch (Exception e) {
      log.error("error sending message", e);
    }
  }
  
  private class GenericMessageEnvelope extends JsonMessageEnvelope {

    private GenericMessageData data;
    
    @Override
    public GenericMessageData getData() {
      return data;
    }

    @Override
    @JsonDeserialize(as=GenericMessageData.class)
    public void setData(MessageData data) {
      this.data = (GenericMessageData) data;
    }

  }
  
  private class GenericMessageData implements MessageData {
    
    private String message;
    
    public GenericMessageData(String message) {
      this.message = message;
    }
    
    @JsonProperty("message")
    public String getMessage() {
      return message;
    }
    
  }
}
