package id.scommon.storage.impl;

import com.google.common.primitives.Longs;

/**
 * Created by sangdn on 10/12/15.
 * An implement of IList64 using standalone Redis
 */
public class RedisList64Impl extends RedisList<Long> {

    public RedisList64Impl(String host, int port, String listName) {
        super(host, port, listName);
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
