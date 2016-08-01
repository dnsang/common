package id.scommon.storage;

import java.io.IOException;
import java.util.List;

/**
 * Created by sangdn on 10/5/15.
 */
public interface INoSQLDB {
    public boolean put(byte[] key, byte[] value);
    public int mput(List<byte[]> key, List<byte[]> value);
    public byte[] get(byte[] key);
    public List<byte[]> mget(List<byte[]> key);
    public boolean isExist(byte[] key);
    public boolean delete(byte[] key);
    public int mdelete(List<byte[]> key);
    public boolean removeAll();
}
