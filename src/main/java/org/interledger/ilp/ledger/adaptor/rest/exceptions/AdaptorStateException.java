package org.interledger.ilp.ledger.adaptor.rest.exceptions;

public class AdaptorStateException extends RuntimeException {

  private static final long serialVersionUID = -1362933879519665520L;

  public AdaptorStateException(String message) {
    super(message);
  }

}
