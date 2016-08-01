package id.scommon.storage.impl;

import com.google.common.primitives.Longs;

/**
 * Created by sangdn on 11/21/15.
 */
public class SsdbList64Impl extends SsdbList<Long> {
    public SsdbList64Impl(String host, int port, int timeoutInMs, String listName) {
        super(host, port, timeoutInMs, listName);
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
