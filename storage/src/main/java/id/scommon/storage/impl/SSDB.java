package id.scommon.storage.impl;

import java.util.List;

import id.scommon.storage.INoSQLDB;

/**
 * Created by SangDang on 8/1/16.
 */
public class SSDB implements INoSQLDB{
  public boolean put(byte[] key, byte[] value) {
    return false;
  }

  public int mput(List<byte[]> key, List<byte[]> value) {
    return 0;
  }

  public byte[] get(byte[] key) {
    return new byte[0];
  }

  public List<byte[]> mget(List<byte[]> key) {
    return null;
  }

  public boolean isExist(byte[] key) {
    return false;
  }

  public boolean delete(byte[] key) {
    return false;
  }

  public int mdelete(List<byte[]> key) {
    return 0;
  }

  public boolean removeAll() {
    return false;
  }
}
