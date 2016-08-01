package id.scommon.storage;

import java.util.List;

/**
 * Created by sangdn on 10/2/15.
 */
public interface IList<T> {
    boolean pushFront(T item);

    boolean pushBack(T item);

    boolean deleteFront();

    boolean deleteBack();

    int size();

    boolean del(T item);

    T get(int pos);

    List<T> get(int pos, int num);

    List<T> rget(int pos, int num);

    boolean insert(int pos, T key);

    boolean set(int pos, T key);

    boolean clear();


}
