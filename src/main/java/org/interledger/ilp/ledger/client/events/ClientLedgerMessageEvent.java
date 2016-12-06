package org.interledger.ilp.ledger.client.events;

import org.interledger.ilp.core.ledger.events.LedgerMessageEvent;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.springframework.context.ApplicationEvent;

public class ClientLedgerMessageEvent 
  extends ApplicationEvent implements LedgerMessageEvent {

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
