package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=JsonRpcConnectNotification.class)
public class JsonRpcConnectNotification extends JsonRpcRequest {
  
}
