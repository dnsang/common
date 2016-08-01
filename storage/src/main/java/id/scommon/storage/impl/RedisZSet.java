package id.scommon.storage.impl;


import id.scommon.storage.IZSet;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by sangdn on 10/12/15.
 * An implement of IList64 using standalone Redis
 */
public abstract class RedisZSet<T> implements IZSet<T> {

    protected String host;
    protected int port;
    protected byte[] zsetName;
    protected Jedis client;
    protected final Logger logger;


    public RedisZSet(String host, int port, String zsetName) {
        this.host = host;
        this.port = port;
        this.zsetName = zsetName.getBytes();
        logger = Logger.getLogger("RZSet:" + this.zsetName);
    }

    public abstract byte[] serialize(T item);

    public abstract T deserialize(byte[] data);

    protected synchronized void initRedisClient() {
        if (client != null) {
            client.close();
        }
        client = new Jedis(host, port);
    }

    public boolean add(T item, double score) {
        try {
            client.zadd(zsetName, score, serialize(item));
            return true;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "add", ex);
            initRedisClient();
        }
        return false;
    }

    public int size() {
        try {
            return client.zcard(zsetName).intValue();

        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "size", ex);
            initRedisClient();
        }
        return -1;
    }

    public boolean increase(T item, double score) {
        try {
            client.zincrby(zsetName, score, serialize(item));
            return true;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "increase", ex);
            initRedisClient();
        }
        return false;
    }
    public boolean decrease(T item, double score) {
        return increase(item, 1-score);
    }

    public boolean set(T item, double score) {
        try {
            Pipeline pipeline = client.pipelined();
            pipeline.zrem(zsetName, serialize(item));
            pipeline.zadd(zsetName, score, serialize(item));
            pipeline.syncAndReturnAll();
            return true;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "set", ex);
            initRedisClient();
        }
        return false;
    }

    public List<T> get(int from, int num) {
        try {
            List<T> result = new LinkedList<T>();
            Set<byte[]> data = client.zrange(zsetName, from, num);
            while (data.iterator().hasNext()) {
                byte[] item = data.iterator().next();
                result.add(deserialize(item));
            }
            return result;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "get", ex);
            initRedisClient();
        }
        return null;
    }

    /**
     * @param from get data from to num zero-based index
     * @param num
     * @return List of Data, Object[0]  = item, Object[1] = score
     */
    public List<Object[]> getWithScore(int from, int num) {
        try {
            List<Object[]> result = new LinkedList<Object[]>();
            Set<Tuple> data = client.zrangeWithScores(zsetName, from, num);
            while (data.iterator().hasNext()) {
                Tuple tupe = data.iterator().next();
                Object[] itemWithScore = new Object[2];
                itemWithScore[0] = deserialize(tupe.getBinaryElement());
                itemWithScore[1] = tupe.getScore();
                result.add(itemWithScore);
            }
            return result;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "getWithScore", ex);
            initRedisClient();
        }
        return null;
    }

    /**
     * @param from position to get from tail to head (zero-based index)
     * @param num  num of item will get
     * @return list unique num of <T> if possible
     * forex: List: 1 2 3 4 5 6
     * rget(0,3) -> 6,5,4
     */
    public List<T> rget(int from, int num) {
        try {
            int size = this.size();
            if (size > 0) {
                int end = size - from - 1;
                int begin = end - num + 1;
                if (begin < 0) begin = 0;

                Set<byte[]> data = client.zrange(zsetName, begin, end);
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

    public List<Object[]> rgetWithScore(int from, int num) {
        try {
            int size = this.size();
            if (size > 0) {
                int end = size - from - 1;
                int begin = end - num + 1;
                if (begin < 0) begin = 0;

                Set<Tuple> data = client.zrangeWithScores(zsetName, begin, end);
                if (data != null) {
                    LinkedList<Object[]> result = new LinkedList<Object[]>();
                    for (Tuple tuple : data) {
                        Object[] itemWithScore = new Object[2];
                        itemWithScore[0] = deserialize(tuple.getBinaryElement());
                        itemWithScore[1] = tuple.getScore();
                        result.addFirst(itemWithScore);
                    }
                    return result;
                }
            } else if (size == 0) {
                return new LinkedList<Object[]>();
            }
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "rgetWithScore", ex);
            initRedisClient();
        }
        return null;
    }

    public boolean remove(T item) {
        try {
            client.zrem(zsetName, serialize(item));
            return true;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "remove", ex);
            initRedisClient();
        }
        return false;
    }

    public boolean removeAll(){
        try {
            client.del(zsetName);
            return true;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "remove", ex);
            initRedisClient();
        }
        return false;
    }
    public int mremove(List<T> items) {
        int nSuccess = 0;
        try {
            for (T item : items) {
                nSuccess += remove(item) ? 1 : 0;
            }
            return nSuccess;
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "remove", ex);
            initRedisClient();
        }
        return nSuccess;
    }

    public int rank(T item) {
        try {
            return client.zrank(zsetName,serialize(item)).intValue();
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "remove", ex);
            initRedisClient();
        }
        return  -1;
    }

    public double getScore(T item) {
        try {
            return client.zscore(zsetName,serialize(item));
        } catch (JedisConnectionException ex) {
            logger.log(Level.SEVERE, "remove", ex);
            initRedisClient();
        }
        return  -1.0f;
    }
}
