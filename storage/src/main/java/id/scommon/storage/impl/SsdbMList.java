package id.scommon.storage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nutz.ssdb4j.SSDBs;
import org.nutz.ssdb4j.spi.Response;
import org.nutz.ssdb4j.spi.SSDB;

import id.scommon.storage.IMList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by sangdn on 11/21/15.
 * An implement of IList using SSDB to persistent to disk
 */
public abstract class SsdbMList<T> implements IMList<T> {
    protected String host;
    protected int port;
    protected int timeoutInMs;
    protected Logger logger = LogManager.getLogger(SsdbMList.class);
    protected SSDB ssdbClient;

    public SsdbMList(String host, int port, int timeoutInMs, String listName){
        this.host = host;
        this.port = port;
        this.timeoutInMs = timeoutInMs;
        initSSDBClient();
    }

    protected synchronized void initSSDBClient(){
        try {
        if(ssdbClient != null){
            ssdbClient.close();
        }
            ssdbClient = SSDBs.pool(host,port,timeoutInMs,null);
        } catch (Exception e) {
            logger.error(e);
        }
    }


    public boolean pushFront(String listName,T item) {
        Response response = ssdbClient.qpush_front(listName, serialize(item));
        return response.ok();
    }

    public boolean pushBack(String listName,T item) {
        Response response = ssdbClient.qpush_back(listName, serialize(item));
        return response.ok();
    }

    public boolean deleteFront(String listName) {
        Response response = ssdbClient.qpop_front(listName);
        return response.ok();
    }

    public boolean deleteBack(String listName) {
        Response response = ssdbClient.qpop_back(listName);
        return response.ok();
    }

    public int size(String listName) {
        Response response = ssdbClient.qsize(listName);
        return response.asInt();
    }

    public boolean del(String listName,T item) {
        throw new UnsupportedOperationException("Not Supported");
    }

    public T get(String listName,int pos) {
        Response response = ssdbClient.qget(listName, pos);
        if(response.ok() && response.datas != null && response.datas.size() == 1){
            return deserialize(response.datas.get(0));
        }
        return null;
    }

    public List<T> get(String listName,int pos, int num) {
        List<T> result = new ArrayList<T>();
        Response response = ssdbClient.qslice(listName, pos, pos + num - 1);
        if(response.ok() && response.datas!= null){
            ArrayList<byte[]> datas = response.datas;
            for (byte[] data : datas){
                result.add(deserialize(data));
            }
        }
        return result;
    }

    public List<T> rget(String listName,int pos, int num) {
        int size = this.size(listName);
        if(size > 0){
            int end = size -pos -1;
            int begin = end - num + 1;
            if(begin <0) begin = 0;

            Response response = ssdbClient.qslice(listName, begin, end);
            if (response.ok() && response.datas != null){
                LinkedList<T> result = new LinkedList<T>();
                for(byte[] item : response.datas){
                    result.addFirst(deserialize(item));
                }
                return result;
            }
        }else if(size ==0){
            return new LinkedList<T>();
        }
        return null;

    }

    public boolean insert(String listName,int pos, T key) {
        throw new UnsupportedOperationException("Upsupported Exception");
    }

    public boolean set(String listName,int pos, T key) {
        throw new UnsupportedOperationException("Upsupported Exception");
    }

    public boolean clear(String listName) {
        Response response = ssdbClient.qclear(listName);
        return response.ok();
    }

    public abstract byte[] serialize(T item);

    public abstract T deserialize(byte[] data);
}
