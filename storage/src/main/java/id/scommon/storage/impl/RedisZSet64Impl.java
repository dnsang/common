package id.scommon.storage.impl;

import com.google.common.primitives.Longs;

/**
 * Created by sangdn on 10/13/15.
 */
public class RedisZSet64Impl extends RedisZSet<Long> {
    public RedisZSet64Impl(String host, int port, String zsetName) {
        super(host, port, zsetName);
    }

    @Override
    public byte[] serialize(Long item) {
        return Longs.toByteArray(item);
    }

    @Override
    public Long deserialize(byte[] data) {
        return Longs.fromByteArray(data);
    }
}
