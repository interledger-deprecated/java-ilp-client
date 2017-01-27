package org.interledger.ilp.client.events;

import java.util.EventObject;

import org.interledger.ilp.ledger.events.LedgerConnectEvent;

public class ClientLedgerConnectEvent 
  extends EventObject implements LedgerConnectEvent {

  private static final long serialVersionUID = 8433132200259576608L;

  public ClientLedgerConnectEvent(Object source) {
    super(source);
  }


}
