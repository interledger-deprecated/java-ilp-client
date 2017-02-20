package org.interledger.ilp.client.events;

import org.interledger.ilp.ledger.events.LedgerConnectEvent;
import org.interledger.ilp.ledger.events.LedgerEvent;
import org.interledger.ilp.ledger.events.LedgerEventHandler;
import org.interledger.ilp.ledger.events.LedgerMessageEvent;
import org.interledger.ilp.ledger.events.LedgerTransferEvent;
import org.interledger.ilp.ledger.model.LedgerMessage;
import org.interledger.ilp.ledger.model.LedgerTransfer;

// TODO: is this still needed?
public abstract class ClientLedgerEventHandler implements LedgerEventHandler {

  protected abstract void onConnect();

  protected abstract void onTransfer(LedgerTransfer ledgerTransfer);

  protected abstract void onMessage(LedgerMessage ledgerMessage);

  protected abstract void onDisconnect();

  @Override
  public void handleLedgerEvent(LedgerEvent event) {

    if (event instanceof LedgerMessageEvent) {
      onMessage(((LedgerMessageEvent) event).getMessage());
    } else if (event instanceof LedgerTransferEvent) {
      onTransfer(((LedgerTransferEvent) event).getTransfer());
    } else if (event instanceof LedgerConnectEvent) {
      onConnect();
    } else {
      throw new RuntimeException("Unknown event: " + event.toString());
    }

  }


}
