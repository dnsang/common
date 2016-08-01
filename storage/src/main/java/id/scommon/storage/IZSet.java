package id.scommon.storage;

import java.util.List;

/**
 * Created by sangdn on 10/2/15.
 */
public interface IZSet<T> {
    public boolean add(T item, double score);
    public int size();
    public boolean increase(T item, double score);
    public boolean decrease(T item, double score);
    public boolean set(T item, double score);
    public List<T> get(int from, int num);
    public List<Object[]> getWithScore(int from, int num);
    public List<T> rget(int from, int num);
    public List<Object[]> rgetWithScore(int from, int num);
    public boolean remove(T item);
    public boolean removeAll();
    public int mremove(List<T> item);
    public int rank(T item);
    public double getScore(T item);

}
