package id.scommon.storage.impl;

import com.google.common.primitives.Longs;


/**
 * Created by sangdn on 11/21/15.
 * An implement of IList using SSDB to persistent to disk
 */
public  class SsdbMList64Impl extends SsdbMList<Long> {

    public SsdbMList64Impl(String host, int port, int timeoutInMs, String listName) {
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
