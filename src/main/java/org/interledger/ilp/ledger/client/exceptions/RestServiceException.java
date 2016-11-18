package org.interledger.ilp.ledger.client.exceptions;

import org.interledger.ilp.ledger.client.rest.json.JsonError;
import org.springframework.web.client.HttpStatusCodeException;

public class RestServiceException extends Exception {

  private static final long serialVersionUID = 427525202912218646L;
  private String id;

  public RestServiceException(JsonError jsonException, HttpStatusCodeException innerException) {
    super(jsonException.getMessage(), innerException);
    this.id = jsonException.getId();
  }

  public String getId() {
    return this.id;
  }


}
