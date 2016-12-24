package org.interledger.ilp.ledger.adaptor.ws;

import java.net.URI;

import org.interledger.ilp.core.ledger.events.LedgerEventHandler;
import org.interledger.ilp.core.ledger.events.LedgerEventSource;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerMessage;
import org.interledger.ilp.ledger.adaptor.rest.json.JsonLedgerTransfer;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcConnectNotification;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotification;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessageNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestTransferNotificationParams;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponseMessage;
import org.interledger.ilp.ledger.client.events.ClientLedgerConnectEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerErrorEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerMessageEvent;
import org.interledger.ilp.ledger.client.events.ClientLedgerTransferEvent;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

public class JsonRpcLedgerWebSocketChannel extends JsonRpcWebSocketChannel implements LedgerEventSource {

  private URI ledgerId;
  private LedgerEventHandler eventHandler;
  private boolean isConnected = false; //Not thread safe?
  
  public JsonRpcLedgerWebSocketChannel(URI ledgerId, URI uri, String authToken, LedgerEventHandler eventHandler) {
    super(URI.create(uri.toString() + "?token=" + authToken), true, 5);
    this.ledgerId = ledgerId;
    this.eventHandler = eventHandler;
  }

  @Override
  public void onConnectionEstablished(WebSocketSession session) {
    super.onConnectionEstablished(session);
    if(!isConnected) {
      eventHandler.handleLedgerEvent(new ClientLedgerConnectEvent(this));
    }
    isConnected = true;
  }

  @Override
  public void onMessage(JsonRpcMessage message) {
    
    super.onMessage(message);

    if(message instanceof JsonRpcNotification) {
      JsonRpcNotificationParams notificatonParams = ((JsonRpcNotification) message).getParams();
      if(notificatonParams instanceof JsonRpcRequestTransferNotificationParams) {
        onLedgerTransferNotification((JsonRpcRequestTransferNotificationParams) notificatonParams);
      } else if(notificatonParams instanceof JsonRpcRequestMessageNotificationParams) {
        onLedgerMessage((JsonRpcRequestMessageNotificationParams) notificatonParams);
      } else {
        onUnknownMessage(message);
      }
    } else if (message instanceof JsonRpcResponseMessage){
      onResponse((JsonRpcResponseMessage) message);
    } else if (message instanceof JsonRpcConnectNotification){
      //We can swallow this because we raise a connect event when we establish the underlying websocket
    } else {
      onUnknownMessage(message);      
    }

  }

  @Override
  public void onTransportError(Throwable exception) {
    eventHandler.handleLedgerEvent(new ClientLedgerErrorEvent(this, exception));
  }
  
  @Override
  public void onResponse(JsonRpcResponseMessage response) {
    super.onResponse(response);
    
  }
  
  @Override
  public void onConnectionClosed(CloseStatus status) {
    isConnected = false;
    super.onConnectionClosed(status);
  }

  @Override
  public URI getLedgerId() {
    return this.ledgerId;
  }
  
  private void onLedgerTransferNotification(JsonRpcRequestTransferNotificationParams transferParams) {
    JsonLedgerTransfer transfer = transferParams.getTransfer();
    eventHandler.handleLedgerEvent(new ClientLedgerTransferEvent(this, transfer.toLedgerTransfer()));
  }
  
  private void onLedgerMessage(JsonRpcRequestMessageNotificationParams messageParams) {
    JsonLedgerMessage msg = ((JsonRpcRequestMessageNotificationParams) messageParams).getMessage();
    eventHandler.handleLedgerEvent(new ClientLedgerMessageEvent(this, msg.toLedgerMessage()));
  }
  
  private void onUnknownMessage(JsonRpcMessage message) {
    eventHandler.handleLedgerEvent(
        new ClientLedgerErrorEvent(this, new Exception("Unrecognized message: " + message.toString())));
  }

}
