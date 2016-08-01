package id.scommon.storage.impl;

import com.google.common.primitives.Ints;

/**
 * Created by sangdn on 10/13/15.
 */
public class RedisZSet32Impl extends RedisZSet<Integer> {

    public RedisZSet32Impl(String host, int port, String zsetName) {
        super(host, port, zsetName);
    }

    @Override
    public byte[] serialize(Integer item) {
        return Ints.toByteArray(item);
    }

    @Override
    public Integer deserialize(byte[] data) {
        return Ints.fromByteArray(data);
    }
}
