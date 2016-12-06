package org.interledger.ilp.ledger.adaptor.ws;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class JsonRpcWebsocketClient extends StandardWebSocketClient {
  
  private String authToken;

  public JsonRpcWebsocketClient(String authToken) {
    super();
    this.authToken = authToken;
  }

  @Override
  protected ListenableFuture<WebSocketSession> doHandshakeInternal(
      WebSocketHandler webSocketHandler, HttpHeaders headers, URI uri, List<String> protocols,
      List<WebSocketExtension> extensions, Map<String, Object> attributes) {

    //Add auth token to URI
    uri = URI.create(uri.toString() + "?token=" + this.authToken);
    
    return super.doHandshakeInternal(webSocketHandler, headers, uri, protocols, extensions, attributes);
  }
  
  

}
