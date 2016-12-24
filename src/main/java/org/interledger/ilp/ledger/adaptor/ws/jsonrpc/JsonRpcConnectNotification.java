package org.interledger.ilp.ledger.adaptor.ws.jsonrpc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as=JsonRpcConnectNotification.class)
@JsonIgnoreProperties(ignoreUnknown=true)
public class JsonRpcConnectNotification extends JsonRpcRequestMessage {
  
}
