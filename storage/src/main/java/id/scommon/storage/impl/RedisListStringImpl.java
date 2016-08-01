package id.scommon.storage.impl;

import java.nio.charset.Charset;

/**
 * Created by sangdn on 10/13/15.
 */
public class RedisListStringImpl extends RedisList<String> {
    public RedisListStringImpl(String host, int port, String listName) {
        super(host, port, listName);
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
