package id.scommon.storage;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sangdn on 10/5/15.
 */
public interface INoSQLDB<K,V> {
    public boolean put(K key, V value);
    public int mput(List<K> key, List<V> value);
    public V get(K key);
    public List<V> mget(List<K> key);
    public boolean isExist(K key);
    public boolean delete(K key);
    public int mdelete(List<K> key);
    public boolean removeAll();
}
