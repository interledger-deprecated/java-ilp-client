package org.interledger.ilp.client;

import org.interledger.ilp.ledger.model.LedgerMessage;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A concurrent hashmap that evicts entries after a timeout. Copied from:
 * http://stackoverflow.com/questions/3802370/java-time-based-map-cache-with-expiring-keys
 */
// TODO: consider replacing custom implementation with something out of the Guava library.
public class LedgerMessageResponseMapper {

  public static final long DEFAULT_EXPIRY_MS = 1000;

  private Map<UUID, BlockingQueue<LedgerMessage>> responseQueues = new ConcurrentHashMap<>();
  private Map<UUID, Long> timeMap = new ConcurrentHashMap<UUID, Long>();
  private long expiryInMillis;

  /**
   * Default constructor. Sets the expiry time to 1000 millis.
   */
  public LedgerMessageResponseMapper() {
    this(DEFAULT_EXPIRY_MS);
  }

  /**
   * Constructs a response mapper using the given expiry time.
   * 
   * @param expiryInMillis The maximum amount of time a request will be held waiting for a response
   *        before being evicted from the cache.
   */
  public LedgerMessageResponseMapper(long expiryInMillis) {
    this.expiryInMillis = expiryInMillis;
    initialize();
  }

  /**
   * Places a request message into the cache.
   * 
   * @param request The request message to cache.
   * @return A queue that can be used to block until the response is received.
   */
  public BlockingQueue<LedgerMessage> storeRequest(LedgerMessage request) {
    if (request.getId() == null) {
      throw new IllegalArgumentException("Request must have an ID.");
    }

    UUID id = request.getId();
    Date date = new Date();
    timeMap.put(id, date.getTime());
    // TODO: shouldnt we return a future instead?
    BlockingQueue<LedgerMessage> messageQueue = new ArrayBlockingQueue<>(1);
    responseQueues.put(id, messageQueue);
    return messageQueue;
  }

  /**
   * Handles response messages that have arrived for a request that may still be in the cache.
   * 
   * @param response The response message received from the ledger.
   */
  public void handleResponse(LedgerMessage response) {
    if (response.getId() == null) {
      throw new IllegalArgumentException("Response must have an ID.");
    }

    UUID id = response.getId();
    BlockingQueue<LedgerMessage> responseQueue = responseQueues.get(id);
    remove(id);
    responseQueue.add(response);
  }

  /**
   * Performs the necessary initialization for the cache.
   */
  protected void initialize() {
    new CleanerThread("LedgerResponseMessageTimeoutMonitor").start();
  }

  /**
   * Removes an item from the cache.
   * 
   * @param id The id of the item to remove.
   */
  protected void remove(UUID id) {
    timeMap.remove(id);
    responseQueues.remove(id);
  }

  /**
   * This thread is responsible for cleaning the cache of expired items.
   */
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
