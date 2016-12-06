package org.interledger.ilp.ledger.client.events;

import org.interledger.ilp.core.ledger.events.LedgerTransferEvent;
import org.interledger.ilp.core.ledger.model.LedgerTransfer;
import org.springframework.context.ApplicationEvent;

public class ClientLedgerTransferEvent 
  extends ApplicationEvent implements LedgerTransferEvent {

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
