package id.scommon.storage.impl;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.WriteBatch;

import id.scommon.storage.INoSQLDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.fusesource.leveldbjni.JniDBFactory.*;

/**
 * Created by sangdn on 10/12/15. An implement of INoSQLDB using single instance levelDB
 */
public final class LevelDB implements INoSQLDB {
  protected DB db = null;
  Options opt = null;
  File file;

  private LevelDB(Options opt, File file) throws IOException {
    this.file = file;
    this.opt = opt;
    initDb();
  }

  protected synchronized void initDb() throws IOException {
    if (db != null) {
      db.close();
    }
    db = factory.open(file, opt);
  }

  public boolean put(byte[] key, byte[] value) {
    try {
      db.put(key, value);
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        initDb();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return false;
    }
    return true;

  }

  public int mput(List<byte[]> key, List<byte[]> value) {

    assert key != null && value != null
        && key.size() == value.size();

    try {
      WriteBatch writeBatch = db.createWriteBatch();

      for (int i = 0; i < key.size(); ++i) {
        writeBatch.put(key.get(i), value.get(i));
      }
      db.write(writeBatch);
    } catch (Exception ex) {
      ex.printStackTrace();
      return -1;
    }
    return 1;
  }

  public byte[] get(byte[] key) {
    return db.get(key);
  }

  public List<byte[]> mget(List<byte[]> key) {
    List<byte[]> result = new ArrayList<byte[]>();
    for (int i = 0; i < key.size(); ++i) {
      try {
        result.add(get(key.get(i)));
      } catch (Exception ex) {
        ex.printStackTrace();
        result.add(null);
      }
    }
    assert result.size() == key.size();
    return result;
  }

  public boolean isExist(byte[] key) {
    try {
      return db.get(key) != null;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public boolean delete(byte[] key) {
    try {
      db.delete(key);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  public int mdelete(List<byte[]> key) {
    WriteBatch writeBatch = db.createWriteBatch();
    try {
      for (int i = 0; i < key.size(); ++i) {
        writeBatch.delete(key.get(i));
      }
      db.write(writeBatch);
    } catch (Exception ex) {
      ex.printStackTrace();
      return -1;
    }
    return 1;

  }

  public boolean removeAll() {
    throw new UnsupportedOperationException("unsupported");
  }

  public static class Builder {
    protected Options opt;

    public Builder() {
      opt = new Options();
    }

    public Builder createIfMissing(boolean value) {
      opt.createIfMissing(value);
      return this;
    }

    public Builder cacheSize(long size) {
      opt.cacheSize(size);
      return this;
    }

    public Builder maxOpenFiles(int num) {
      opt.maxOpenFiles(num);
      return this;
    }

    public LevelDB build(String storageDirectory) throws IOException {
      File file = new File(storageDirectory);
      if (file.isFile()) {
        throw new IOException("expect a folder but got a exist file");
      }
      if (!file.exists()) file.mkdirs();
      return new LevelDB(this.opt, file);
    }
  }
}
