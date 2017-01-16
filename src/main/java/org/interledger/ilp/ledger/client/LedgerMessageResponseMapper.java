package org.interledger.ilp.ledger.client;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.interledger.ilp.ledger.client.json.JsonMessageEnvelope;

/**
 * A concurrent hashmap that evicts entries after a timeout.
 * 
 * Copied from:
 * http://stackoverflow.com/questions/3802370/java-time-based-map-cache-with-expiring-keys
 */
public class LedgerMessageResponseMapper {

  private Map<String, BlockingQueue<JsonMessageEnvelope>> responseQueues = new ConcurrentHashMap<>();
  private Map<String, Long> timeMap = new ConcurrentHashMap<String, Long>();
  private long expiryInMillis = 1000;

  public LedgerMessageResponseMapper() {
    initialize();
  }

  public LedgerMessageResponseMapper(long expiryInMillis) {
    this.expiryInMillis = expiryInMillis;
    initialize();
  }

  void initialize() {
    new CleanerThread("LedgerResponseMessageTimeoutMonitor").start();
  }

  public BlockingQueue<JsonMessageEnvelope> storeRequest(JsonMessageEnvelope request) {

    if (request.getId() == null) {
      throw new IllegalArgumentException("Request must have an ID.");
    }

    String id = request.getId();
    Date date = new Date();
    timeMap.put(id, date.getTime());
    BlockingQueue<JsonMessageEnvelope> q = new ArrayBlockingQueue<>(1);
    responseQueues.put(id, q);
    return q;
  }

  public void handleResponse(JsonMessageEnvelope response) {

    if (response.getId() == null) {
      throw new IllegalArgumentException("Response must have an ID.");
    }

    String id = response.getId();
    BlockingQueue<JsonMessageEnvelope> responseQueue = responseQueues.get(id);
    remove(id);
    responseQueue.add(response);

  }

  private void remove(String id) {
    timeMap.remove(id);
    responseQueues.remove(id);
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
          remove(id);
        }
      }
    }
  }

}
