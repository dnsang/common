package id.scommon.storage.impl;

import com.google.common.primitives.Ints;

/**
 * Created by sangdn on 10/13/15.
 */
public class RedisList32Impl extends RedisList<Integer>{
    public RedisList32Impl(String host, int port, String listName) {
        super(host, port, listName);
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
