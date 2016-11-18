package org.interledger.ilp.client.events;

import java.util.EventObject;

import org.interledger.ilp.ledger.events.LedgerTransferEvent;
import org.interledger.ilp.ledger.model.LedgerTransfer;

public class ClientLedgerTransferEvent 
  extends EventObject implements LedgerTransferEvent {

  private static final long serialVersionUID = -941813660810194734L;
  
  private LedgerTransfer transfer;

  public ClientLedgerTransferEvent(Object source, LedgerTransfer transfer) {
    super(source);
    this.transfer = transfer;
  }

  @Override
  public LedgerTransfer getTransfer() {
    return transfer;
  }

}
