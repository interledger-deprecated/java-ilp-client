package org.interledger.ilp.ledger.adaptor.rest.exceptions;

import org.interledger.ilp.ledger.adaptor.rest.json.JsonError;
import org.springframework.web.client.HttpStatusCodeException;

public class RestServiceException extends RuntimeException {

  private static final long serialVersionUID = 427525202912218646L;
  private String id;

  public RestServiceException(JsonError jsonException, HttpStatusCodeException innerException) {
    super(jsonException.getMessage(), innerException);
    this.id = jsonException.getId();
  }

  public RestServiceException(String message, HttpStatusCodeException innerException) {
    super(message, innerException);
    //TODO What do we do about an ID?
  }

  public String getId() {
    return this.id;
  }


}
