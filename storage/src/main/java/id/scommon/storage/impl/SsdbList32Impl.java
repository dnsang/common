package id.scommon.storage.impl;

import com.google.common.primitives.Ints;

/**
 * Created by sangdn on 11/21/15.
 */
public class SsdbList32Impl extends SsdbList<Integer> {

    public SsdbList32Impl(String host, int port, int timeoutInMs, String listName) {
        super(host, port, timeoutInMs, listName);
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
