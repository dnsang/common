package id.scommon.storage.impl;

import java.nio.charset.Charset;

/**
 * Created by sangdn on 10/13/15.
 */
public class RedisZSetStringImpl extends RedisZSet<String> {
    public RedisZSetStringImpl(String host, int port, String zsetName) {
        super(host, port, zsetName);
    }

    @Override
    public byte[] serialize(String item) {
        return item.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public String deserialize(byte[] data) {
        return new String(data);
    }
}
