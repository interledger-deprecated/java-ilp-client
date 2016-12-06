package org.interledger.ilp.ledger.client.events;

import org.interledger.ilp.core.ledger.events.LedgerConnectEvent;
import org.springframework.context.ApplicationEvent;

public class ClientLedgerConnectEvent 
  extends ApplicationEvent implements LedgerConnectEvent {

  private static final long serialVersionUID = 8433132200259576608L;

  public ClientLedgerConnectEvent(Object source) {
    super(source);
  }


}
