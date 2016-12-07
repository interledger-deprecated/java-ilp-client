package org.interledger.ilp.ledger.adaptor.ws;

import java.net.URI;

import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.events.LedgerEventSource;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransferAccountEntry;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotification;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessageNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestTransferNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponse;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerMessageEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerTransferEvent;
import org.interledger.ilp.ledger.client.model.ClientLedgerTransfer;
import org.springframework.web.socket.CloseStatus;

public class JsonRpcLedgerWebSocketChannel extends JsonRpcWebSocketChannel implements LedgerEventSource {

  private URI ledgerId;
  private LedgerEventHandler eventHandler;
  
  public JsonRpcLedgerWebSocketChannel(URI ledgerId, URI uri, String authToken, LedgerEventHandler eventHandler) {
    super(URI.create(uri.toString() + "?token=" + authToken));
    this.ledgerId = ledgerId;
    this.eventHandler = eventHandler;
  }

  @Override
  public void onConnectionEstablished() {
    eventHandler.handleLedgerEvent(new ClientLedgerConnectEvent(this));
    super.onConnectionEstablished();
  }

  @Override
  public void onJsonRpcMessage(JsonRpcMessage message) {
    
    if(message instanceof JsonRpcNotification) {
      
      JsonRpcNotificationParams notificatonParams = ((JsonRpcNotification) message).getParams();
      
      if(notificatonParams instanceof JsonRpcRequestTransferNotificationParams) {
        JsonLedgerTransfer transfer = ((JsonRpcRequestTransferNotificationParams) notificatonParams).getTransfer();
        eventHandler.handleLedgerEvent(new ClientLedgerTransferEvent(this, buildClientLedgerTransfer(transfer)));
      } else if(notificatonParams instanceof JsonRpcRequestMessageNotificationParams) {
        JsonLedgerMessage msg = ((JsonRpcRequestMessageNotificationParams) notificatonParams).getMessage();
        eventHandler.handleLedgerEvent(new ClientLedgerMessageEvent(this, msg));
      } else {
        //TODO emit error event
      }
    } else if (message instanceof JsonRpcResponse){
      //TODO emit event
      
    }
    
    //logger.warn("Received unknown json-rpc message with id: {}", message.getId());

    super.onJsonRpcMessage(message);
  }

  @Override
  public void onJsonRpcTransportError(Throwable exception) {
    super.onJsonRpcTransportError(exception);
//    TODO    
//    eventHandler.handleLedgerEvent(new LedgerClientErrorEvent(this, exception));

  }

  @Override
  public void onConnectionClosed(CloseStatus status) {
    // TODO Auto-generated method stub
    super.onConnectionClosed(status);
  }

  @Override
  public URI getLedgerId() {
    return this.ledgerId;
  }
  
  //TODO Extract to stand-alone converter service
  private ClientLedgerTransfer buildClientLedgerTransfer(JsonLedgerTransfer jsonTransfer) {
    
    if(jsonTransfer.getCredits().size() != 1 || jsonTransfer.getDebits().size() != 1) {
      throw new RuntimeException("Only single transaction transfers are supported.");
    }
    
    JsonLedgerTransferAccountEntry creditEntry = jsonTransfer.getCredits().get(0);
    JsonLedgerTransferAccountEntry debitEntry = jsonTransfer.getDebits().get(0);
    
    ClientLedgerTransfer transfer = new ClientLedgerTransfer();
    transfer.setId(jsonTransfer.getId().toString());
    transfer.setLedgerId(jsonTransfer.getLedgerId().toString());
    
    transfer.setToAccount(creditEntry.getAccount().toString());

    //FIXME Should we check that there are no inconsistencies between the debit and credit entries
    transfer.setFromAccount(debitEntry.getAccount().toString());
    transfer.setAmount(debitEntry.getAmount());
    transfer.setAuthorized(debitEntry.isAuthorized());
    if(debitEntry.getInvoice() != null) {
      transfer.setInvoice(debitEntry.getInvoice().toString());
    }
    transfer.setMemo(debitEntry.getMemo());
    transfer.setRejected(debitEntry.isRejected());
    transfer.setRejectionMessage(debitEntry.getRejectionMessage());
    
    transfer.setCancellationCondition(jsonTransfer.getCancellationCondition());
    transfer.setExecutionCondition(jsonTransfer.getExecutionCondition());
    transfer.setExpiresAt(jsonTransfer.getExpiresAt());

    return transfer;
  }

}
