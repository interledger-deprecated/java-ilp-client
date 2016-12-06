package org.interledger.ilp.ledger.adaptor.ws;

import java.net.URI;

import org.interledger.ilp.core.ledger.events.LedgerEvent;
import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.events.LedgerEventSource;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotification;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessageNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestTransferNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponse;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerMessageEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerTransferEvent;
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
        eventHandler.handleLedgerEvent(new ClientLedgerTransferEvent(this, transfer));
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
  
  

}
