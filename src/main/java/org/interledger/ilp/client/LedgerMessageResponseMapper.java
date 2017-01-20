package org.interledger.ilp.client;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import org.interledger.ilp.core.ledger.model.LedgerMessage;

/**
 * A concurrent hashmap that evicts entries after a timeout.
 * 
 * Copied from:
 * http://stackoverflow.com/questions/3802370/java-time-based-map-cache-with-expiring-keys
 */
public class LedgerMessageResponseMapper {

  private Map<UUID, BlockingQueue<LedgerMessage>> responseQueues = new ConcurrentHashMap<>();
  private Map<UUID, Long> timeMap = new ConcurrentHashMap<UUID, Long>();
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

  public BlockingQueue<LedgerMessage> storeRequest(LedgerMessage request) {

    if (request.getId() == null) {
      throw new IllegalArgumentException("Request must have an ID.");
    }

    UUID id = request.getId();
    Date date = new Date();
    timeMap.put(id, date.getTime());
    BlockingQueue<LedgerMessage> q = new ArrayBlockingQueue<>(1);
    responseQueues.put(id, q);
    return q;
  }

  public void handleResponse(LedgerMessage response) {

    if (response.getId() == null) {
      throw new IllegalArgumentException("Response must have an ID.");
    }

    UUID id = response.getId();
    BlockingQueue<LedgerMessage> responseQueue = responseQueues.get(id);
    remove(id);
    responseQueue.add(response);

  }

  private void remove(UUID id) {
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
      for (UUID id : timeMap.keySet()) {
        if (currentTime > (timeMap.get(id) + expiryInMillis)) {
          remove(id);
        }
      }
    }
  }

}
