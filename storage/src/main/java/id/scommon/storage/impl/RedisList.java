package id.scommon.storage.impl;


import id.scommon.storage.IList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by sangdn on 10/12/15. An implement of IList64 using standalone Redis
 */
public abstract class RedisList<T> implements IList<T> {
  protected String host;
  protected int port;
  protected byte[] listName;
  protected Jedis client;
  protected final Logger logger;


  public RedisList(String host, int port, String listName) {
    this.host = host;
    this.port = port;
    this.listName = listName.getBytes();
    initRedisClient();
    logger = Logger.getLogger("RList:" + listName);
  }

  public abstract byte[] serialize(T item);

  public abstract T deserialize(byte[] data);

  protected synchronized void initRedisClient() {
    if (client != null) {
      client.close();
    }
    client = new Jedis(host, port);
  }

  /**
   * @param item to add to head of list
   */
  public boolean pushFront(T item) {
    try {
      client.lpush(listName, serialize(item));
      return true;
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "pushFront", ex);
      initRedisClient();
    }
    return false;
  }

  public boolean pushBack(T item) {
    try {
      client.rpush(listName, serialize(item));
      return true;
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "pushBack", ex);
      initRedisClient();
    }
    return false;
  }

  public boolean deleteFront() {
    try {
      client.lpop(listName);
      return true;
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "deleteFront", ex);
      initRedisClient();
    }
    return false;

  }

  public boolean deleteBack() {
    try {
      client.rpop(listName);
      return true;
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "deleteBack", ex);
      initRedisClient();
    }
    return false;
  }

  public int size() {
    try {
      return client.llen(listName).intValue();
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "size", ex);
      initRedisClient();
    }
    return -1;

  }

  public boolean del(T item) {
    try {
      client.lrem(listName, 1, serialize(item));
      return true;
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "del", ex);
      initRedisClient();
    }
    return false;

  }


  /**
   * @param pos: position in list, from 0
   * @return data or null
   */
  public T get(int pos) {
    try {
      List<byte[]> data = client.lrange(listName, pos, pos);
      if (data != null && data.size() == 1) return deserialize(data.get(0));
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "get", ex);
      initRedisClient();
    }
    return null;
  }

  /**
   * @param pos position to get from head to tail (zero-based index)
   * @param num num of item will get
   * @return list num of <T> if possible forex: List: 1 2 3 4 5 6 get(0,3) -> 1,2,3
   */
  public List<T> get(int pos, int num) {
    try {
      List<byte[]> data = client.lrange(listName, pos, pos + num - 1);
      if (data != null) {
        List<T> result = new LinkedList<T>();
        for (byte[] item : data) {
          result.add(deserialize(item));
        }
        return result;
      }
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "get", ex);
      initRedisClient();
    }
    return null;
  }

  /**
   * @param pos position to get from tail to head (zero-based index)
   * @param num num of item will get
   * @return list num of <T> if possible forex: List: 1 2 3 4 5 6 rget(0,3) -> 6,5,4
   */
  public List<T> rget(int pos, int num) {
    try {
      int size = this.size();
      if (size > 0) {
        int end = size - pos - 1;
        int begin = end - num + 1;
        if (begin < 0) begin = 0;

        List<byte[]> data = client.lrange(listName, begin, end);
        if (data != null) {
          LinkedList<T> result = new LinkedList<T>();
          for (byte[] item : data) {
            result.addFirst(deserialize(item));
          }
          return result;
        }
      } else if (size == 0) {
        return new LinkedList<T>();
      }

    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "rget", ex);
      initRedisClient();
    }
    return null;
  }

  public boolean insert(int pos, T key) {
    throw new UnsupportedOperationException("not implement yet");
  }

  public boolean set(int pos, T key) {
    throw new UnsupportedOperationException("not implement yet");
  }

  public boolean clear() {
    try {
      client.del(listName);
      return true;
    } catch (JedisConnectionException ex) {
      logger.log(Level.SEVERE, "clear", ex);
      initRedisClient();

    }
    return false;
  }
}
