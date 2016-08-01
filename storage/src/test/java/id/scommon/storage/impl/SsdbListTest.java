package id.scommon.storage.impl;

import org.junit.Assert;

import java.util.List;

/**
 * Created by sangdn on 11/21/15.
 */
public class SsdbListTest {
    public static void main(String[] args) {
        SsdbList32Impl list32 = new SsdbList32Impl("127.0.0.1",8888,1000,"test");

        testCRUD(list32);
    }

    public static void testCRUD(SsdbList32Impl list){
        Assert.assertTrue(list.clear());
        Assert.assertTrue(list.pushFront(1));
        Assert.assertTrue(list.get(0) == 1);
        Assert.assertTrue(list.pushBack(2));
        Assert.assertTrue(list.size() == 2);
        List<Integer> result = list.get(0, 2);
        Assert.assertTrue(result.size() == 2);

        list.clear();
        for(int i =0; i < 10; ++i){
            list.pushBack(i);
        }
        Assert.assertTrue(list.size() == 10);
        Assert.assertTrue(list.get(3) == 3);
        List<Integer> integers = list.get(1, 3);
        Assert.assertTrue(integers.size()==3);
        Assert.assertTrue(integers.get(0)==1);
        Assert.assertTrue(integers.get(1)==2);
        Assert.assertTrue(integers.get(2)==3);


    }
}
