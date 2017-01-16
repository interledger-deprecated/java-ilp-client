package org.interledger.ilp.ledger.client.exceptions;

public class ResponseTimeoutException extends Exception {

  public ResponseTimeoutException(String message) {
    super(message);
  }

  private static final long serialVersionUID = 7225426336113708854L;

}
