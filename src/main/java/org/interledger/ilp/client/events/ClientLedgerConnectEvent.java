package org.interledger.ilp.client.events;

import org.interledger.ilp.ledger.events.LedgerConnectEvent;

import java.util.EventObject;

public class ClientLedgerConnectEvent extends EventObject implements LedgerConnectEvent {

  private static final long serialVersionUID = 8433132200259576608L;

  public ClientLedgerConnectEvent(Object source) {
    super(source);
  }

}
