package org.interledger.ilp.ledger.client.events;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;
import org.springframework.context.ApplicationEvent;

public class LedgerNotificationEvent 
  extends ApplicationEvent implements MessageEvent<Notification> {

  private static final long serialVersionUID = -3783517030193972130L;
  
  private Notification message;

  public LedgerNotificationEvent(Object source, Notification message) {
    super(source);
    this.message = message;
  }

  @Override
  public Notification getMessage() {
    return message;
  }

}
