package org.interledger.ilp.ledger.client.events;

import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.springframework.context.ApplicationEvent;

public class ClientLedgerErrorEvent extends ApplicationEvent implements LedgerEvent {

  private static final long serialVersionUID = -3659190466620591845L;
  
  private Throwable error;
  
  public ClientLedgerErrorEvent(Object source, Throwable error) {
    super(source);
    this.error = error;
  }

  public Throwable getError() {
    return this.error;
  }
  
}
