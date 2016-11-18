package org.interledger.ilp.ledger.client.rest.service;

import org.interledger.ilp.core.ledger.events.MessageEvent;
import org.interledger.ilp.core.ledger.model.Notification;
import org.interledger.ilp.core.ledger.service.LedgerMessageService;
import org.springframework.context.event.EventListener;

public class RestLedgerMessageService implements LedgerMessageService {

  @Override
  public boolean sendNotification(Notification message) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  @EventListener
  public void onNotificationMessageReceived(MessageEvent<Notification> event) {
    // TODO Auto-generated method stub
    
  }
}
