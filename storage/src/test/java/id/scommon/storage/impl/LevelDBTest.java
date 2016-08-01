package id.scommon.storage.impl;

import org.junit.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by sangdn on 10/12/15.
 */
public class LevelDBTest {
    static LevelDB db = null;

    @BeforeClass
    public static void setUp() throws Exception {
        String file = "/tmp/leveldb-test";

        db = new LevelDB.Builder()
                .createIfMissing(true)
                .maxOpenFiles(1000)
                .cacheSize(100)
                .build(file);


    }

    @AfterClass
    public static void tearDown() throws Exception {
        String file = "/tmp/leveldb-test";
        File f = new File(file);
        if(f.exists()) f.delete();
    }

    protected Queue<Object[]> generateDataToTest(int numItem) {
        Queue<Object[]> _dataPut = new LinkedList<Object[]>();
        for (int i = 0; i < numItem; i++) {
            Object[] obj = new Object[2];
            obj[0] = (Long) ThreadLocalRandom.current().nextLong();
            obj[1] = (Long) ThreadLocalRandom.current().nextLong();

            _dataPut.add(obj);
        }
        return _dataPut;
    }

    //    @Test
    public void testPut() throws Exception {
        db.put("key 1".getBytes(), "value 1".getBytes());
        assert db.isExist("key 1".getBytes());
        final int numThread = 100;
        final int numAction = 1000;
        final Queue<Object[]> _data = generateDataToTest(100000000);
        //ToDo Parrarell put + get
        final Runnable putTest = new Runnable() {
            public void run() {
                _data.peek();
            }
        };

        Thread t[] = new Thread[numThread];
        for (int i = 0; i < numThread; ++i) {
            t[i] = new Thread(new Runnable() {
                public void run() {

                }
            });
        }
    }

    @Test
    public void testMput() throws Exception {
        List<byte[]> lsKey = new ArrayList<byte[]>();
        lsKey.add("key 2".getBytes());
        lsKey.add("key 3".getBytes());
        List<byte[]> lsValue = new ArrayList<byte[]>();
        lsValue.add("value 2".getBytes());
        lsValue.add("value 3".getBytes());

        db.mput(lsKey, lsValue);

        assert db.isExist("key 2".getBytes());
        assert db.isExist("key 3".getBytes());
        assert !db.isExist("key2".getBytes());
    }

    @Test
    public void testGet() throws Exception {
        db.put("key 4".getBytes(), "value 4".getBytes());
        db.put("key 5".getBytes(), "value 5".getBytes());

        assert Arrays.equals(db.get("key 4".getBytes()), "value 4".getBytes());
        assert Arrays.equals(db.get("key 5".getBytes()), "value 5".getBytes());

    }

    @Test
    public void testMget() throws Exception {
        db.put("key 6".getBytes(), "value 6".getBytes());
        db.put("key 7".getBytes(), "value 7".getBytes());
        db.put("key 8".getBytes(), "value 8".getBytes());
        db.put("key 9".getBytes(), "value 9".getBytes());
        List<byte[]> lsKey = new ArrayList<byte[]>();
        lsKey.add("key 6".getBytes());
        lsKey.add("key 7".getBytes());
        lsKey.add("key 8".getBytes());
        lsKey.add("key 9".getBytes());
        List<byte[]> lsValue = db.mget(lsKey);
        assert lsValue.size() == 4;
        assert Arrays.equals(lsValue.get(0), "value 6".getBytes());
        assert Arrays.equals(lsValue.get(1), "value 7".getBytes());
        assert Arrays.equals(lsValue.get(2) , "value 8".getBytes());
        assert Arrays.equals(lsValue.get(3) , "value 9".getBytes());
    }

    @Test
    public void testIsExist() throws Exception {
        db.put("key 10".getBytes(), "value 10".getBytes());
        assert db.isExist("key 10".getBytes());
    }

    @Test
    public void testDelete() throws Exception {
        db.put("key 11".getBytes(), "value 11".getBytes());
        db.put("key 12".getBytes(), "value 12".getBytes());
        db.put("key 13".getBytes(), "value 13".getBytes());
        db.put("key 14".getBytes(), "value 14".getBytes());

        assert db.isExist("key 11".getBytes());
        db.delete("key 11".getBytes());
        assert !db.isExist("key 11".getBytes());

        assert db.isExist("key 12".getBytes());
        db.delete("key 12".getBytes());
        assert !db.isExist("key 12".getBytes());

        assert db.isExist("key 13".getBytes());
        db.delete("key 13".getBytes());
        assert !db.isExist("key 13".getBytes());

        assert db.isExist("key 14".getBytes());
        db.delete("key 14".getBytes());
        assert !db.isExist("key 14".getBytes());
    }

    @Test
    public void testMdelete() throws Exception {
        db.put("key 15".getBytes(), "value 15".getBytes());
        db.put("key 16".getBytes(), "value 16".getBytes());
        db.put("key 17".getBytes(), "value 17".getBytes());
        db.put("key 18".getBytes(), "value 18".getBytes());
        assert db.isExist("key 15".getBytes());
        assert db.isExist("key 16".getBytes());
        assert db.isExist("key 17".getBytes());
        assert db.isExist("key 18".getBytes());

        List<byte[]> lsKey = new ArrayList<byte[]>();
        lsKey.add("key 16".getBytes());
        lsKey.add("key 17".getBytes());
        lsKey.add("key 18".getBytes());

        db.mdelete(lsKey);

        assert db.isExist("key 15".getBytes());
        assert !db.isExist("key 16".getBytes());
        assert !db.isExist("key 17".getBytes());
        assert !db.isExist("key 18".getBytes());
    }


}