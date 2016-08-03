package id.scommon.storage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.Response;

import java.util.ArrayList;
import java.util.List;

import id.scommon.storage.IKeyValueDB;
import id.scommon.storage.INoSQLDB;

/**
 * Created by SangDang on 8/1/16.
 */
public class SSDB implements IKeyValueDB {
  protected String host;
  protected int port;
  protected int timeoutInMs;
  protected org.nutz.ssdb4j.spi.SSDB ssdbClient;

  public SSDB(String host, int port, int timeoutInMs) {
    this.host = host;
    this.port = port;
    this.timeoutInMs = timeoutInMs;
    initSSDBClient();
  }

  protected synchronized void initSSDBClient() {
    try {
      if (ssdbClient != null) {
        ssdbClient.close();
      }
      ssdbClient = SSDBs.pool(host, port, timeoutInMs, null);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean put(byte[] key, byte[] value) {
    return ssdbClient.set(key, value).ok();
  }

  public int mput(List<byte[]> key, List<byte[]> value) {
    if (key == null || value == null || key.size() != value.size()) return -2;
    List<byte[]> pairs = new ArrayList<byte[]>();
    for (int i = 0; i < key.size(); ++i) {
      pairs.add(key.get(i));
      pairs.add(value.get(i));
    }
    Response response = ssdbClient.multi_set(pairs);
    return response.ok() == true ? 1 : -1;
  }

  public byte[] get(byte[] key) {
    return ssdbClient.get(key).datas.get(0);
  }

  public List<byte[]> mget(List<byte[]> key) {
    return ssdbClient.multi_get(key).datas;
  }

  public boolean isExist(byte[] key) {
    return ssdbClient.exists(key).asInt() == 1;
  }

  public boolean delete(byte[] key) {
    return ssdbClient.del(key).ok();
  }

  public int mdelete(List<byte[]> key) {
    return ssdbClient.multi_del(key).asInt();
  }

  public boolean removeAll() {
    return false;
  }
}
