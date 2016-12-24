package org.interledger.ilp.ledger.adaptor.ws;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcError;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcRequestMessage;
import org.interledger.ilp.ledger.adaptor.ws.jsonrpc.JsonRpcResponseMessage;

/**
 * A concurrent hashmap that evicts entries after a timeout.
 * 
 * Copied from:
 * http://stackoverflow.com/questions/3802370/java-time-based-map-cache-with-expiring-keys
 */
public class JsonRpcRequestResponseMapper {

  private Map<String, JsonRpcRequestMessage> requestMap = new ConcurrentHashMap<String, JsonRpcRequestMessage>();
  private Map<String, JsonRpcResponseHandler> handlerMap =
      new ConcurrentHashMap<String, JsonRpcResponseHandler>();
  private Map<String, Long> timeMap = new ConcurrentHashMap<String, Long>();
  private long expiryInMillis = 1000;

  public JsonRpcRequestResponseMapper() {
    initialize();
  }

  public JsonRpcRequestResponseMapper(long expiryInMillis) {
    this.expiryInMillis = expiryInMillis;
    initialize();
  }

  void initialize() {
    new CleanerThread("JsonRpcResponseTimeoutMonitor").start();
  }

  public JsonRpcResponseHandler storeRequest(JsonRpcRequestMessage request,
      JsonRpcResponseHandler responseHandler) {

    String id = request.getId();
    if (id == null) {
      throw new IllegalArgumentException("Request must have an ID.");
    }

    Date date = new Date();
    timeMap.put(id, date.getTime());
    requestMap.put(id, request);
    return handlerMap.put(id, responseHandler);
  }

  public void handleResponse(JsonRpcResponseMessage response) {
    String id = response.getId();
    if (id == null) {
      throw new IllegalArgumentException("Response must have an ID.");
    }

    JsonRpcRequestMessage request = requestMap.get(id);
    JsonRpcResponseHandler responseHandler = handlerMap.get(id);
    remove(id);
    
    if (responseHandler != null) {
      responseHandler.handleResponse(request, response);
    }

  }

  private void remove(String id) {
    timeMap.remove(id);
    handlerMap.remove(id);
    requestMap.remove(id);
  }

  class CleanerThread extends Thread {
    public CleanerThread(String string) {
      super(string);
    }

    @Override
    public void run() {
      while (true) {
        cleanMap();
        try {
          Thread.sleep(expiryInMillis / 2);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }

    private void cleanMap() {
      long currentTime = new Date().getTime();
      for (String id : timeMap.keySet()) {
        if (currentTime > (timeMap.get(id) + expiryInMillis)) {
          JsonRpcRequestMessage request = requestMap.get(id);
          JsonRpcResponseHandler responseHandler = handlerMap.get(id);
          if (responseHandler != null) {
            JsonRpcResponseMessage response = new JsonRpcResponseMessage();
            JsonRpcError error = new JsonRpcError();
            error.setCode(0); // TODO Use correct code
            error.setMessage("Timed out waiting for response to request. id: " + id);
            response.setId(id);
            response.setError(error);
            responseHandler.handleResponse(request, response);
          }
          remove(id);
        }
      }
    }
  }

}
