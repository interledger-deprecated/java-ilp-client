package org.interledger.ilp.ledger.client;

import org.interledger.ilp.core.ledger.events.LedgerConnectEvent;
import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.events.LedgerMessageEvent;
import org.interledger.ilp.core.ledger.events.LedgerTransferEvent;
import org.interledger.ilp.core.ledger.model.LedgerMessage;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;

public abstract class ClientLedgerEventHandler implements LedgerEventHandler {

  protected abstract void onConnect();

  protected abstract void onTransfer(LedgerTransfer ledgerTransfer);

  protected abstract void onMessage(LedgerMessage ledgerMessage);
  
  protected abstract void onDisconnect();

  @Override
  public void handleLedgerEvent(LedgerEvent event) {
    
    if(event instanceof LedgerMessageEvent) {
      onMessage(((LedgerMessageEvent) event).getMessage());
    } else if(event instanceof LedgerTransferEvent) {
      onTransfer(((LedgerTransferEvent) event).getTransfer());
    } else if(event instanceof LedgerConnectEvent) {
      onConnect();
    } else {
      throw new RuntimeException("Unknown event: " + event.toString());
    }
    
  }

  
}
