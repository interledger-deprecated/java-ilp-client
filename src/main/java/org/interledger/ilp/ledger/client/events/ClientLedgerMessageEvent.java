package org.interledger.ilp.ledger.client.events;

import java.util.EventObject;

import org.interledger.ilp.core.ledger.events.LedgerMessageEvent;
import org.interledger.ilp.core.ledger.model.LedgerMessage;

public class ClientLedgerMessageEvent 
  extends EventObject implements LedgerMessageEvent {

  private static final long serialVersionUID = -3783517030193972130L;
  
  private LedgerMessage message;

  public ClientLedgerMessageEvent(Object source, LedgerMessage message) {
    super(source);
    this.message = message;
  }

  @Override
  public LedgerMessage getMessage() {
    return message;
  }

}
