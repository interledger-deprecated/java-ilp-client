package org.interledger.ilqp.client.model;

import org.interledger.ilqp.core.model.QuoteErrorResponse;

public class ClientQuoteErrorResponse implements QuoteErrorResponse {

  private String id;
  private String message;
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
}
