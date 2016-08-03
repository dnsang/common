package id.scommon.storage;

import com.google.gson.JsonObject;

import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SangDang on 8/3/16.
 */
public interface IDocumentDB extends INoSQLDB<String, JsonObject> {
  public void upsert(String key, JsonObject value);

  public boolean update(String key, String field, Object value);

  public boolean increase(String key, String field, long delta);

  public Iterator<JsonObject> sql(String query);
}
