package org.interledger.ilp.ledger.client.json;

import org.interledger.ilp.core.client.model.MessageData;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class JsonQuoteResponseEnvelope extends JsonMessageEnvelope {

  private JsonQuoteResponse quote;
    
  @Override
  public JsonQuoteResponse getData() {
    return quote;
  }

  @Override
  @JsonDeserialize(as=JsonQuoteResponse.class)
  public void setData(MessageData data) {
    if(!(data instanceof JsonQuoteResponse)) {
      throw new IllegalArgumentException("Only Quote Request objects accepted as data.");
    }
    
    quote = (JsonQuoteResponse) data;
    
  }

}
