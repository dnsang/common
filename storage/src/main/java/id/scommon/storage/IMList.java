package id.scommon.storage;

import java.util.List;

/**
 * Created by sangdn on 10/2/15.
 */
public interface IMList<T> {
    boolean pushFront(String listName,T item);

    boolean pushBack(String listName,T item);

    boolean deleteFront(String listName);

    boolean deleteBack(String listName);

    int size(String listName);

    boolean del(String listName,T item);

    T get(String listName,int pos);

    List<T> get(String listName,int pos, int num);

    List<T> rget(String listName,int pos, int num);

    boolean insert(String listName,int pos, T key);

    boolean set(String listName,int pos, T key);

    boolean clear(String listName);


}
