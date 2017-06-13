package com.eaglesakura.collection;

import com.eaglesakura.lambda.Matcher1;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class DataCollectionTest {

    final List<Integer> mOriginalData = Arrays.asList(4, 1, 3, 2, 6, 0, 5, 8, 7, 9);

    final Matcher1 mFilter = new Matcher1() {
        @Override
        public boolean match(Object it) throws Exception {
            return (int) it % 2 == 0;
        }
    };

    DataCollection<Integer> newTestData() {
        return new DataCollection<>(mOriginalData)
                .setComparator(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return Integer.compare(o1, o2);
                    }
                });
    }

    @Test
    public void フィルタをかけて取得できる() {
        DataCollection<Integer> collection = newTestData();

        List<Integer> integers = collection.list(mFilter);

        assertNotEquals(integers, mOriginalData);
        assertEquals(integers.size(), 5);
        assertEquals((int) integers.get(0), 0);
        assertEquals((int) integers.get(4), 8);
    }

    @Test
    public void 最初に引っかかったオブジェクトを取得できる() {
        DataCollection<Integer> collection = newTestData();

        assertEquals((int) collection.find(mFilter), 4);
    }

    @Test
    public void n番目に引っかかったオブジェクトを取得できる() {
        DataCollection<Integer> collection = newTestData();

        assertEquals((int) collection.find(mFilter, 2), 6);
    }

    @Test
    public void ソートして最初に引っかかったオブジェクトを取得できる() {
        DataCollection<Integer> collection = newTestData();

        assertEquals((int) collection.findWithSort(mFilter), 0);
    }

    @Test
    public void ソートしてn番目に引っかかったオブジェクトを取得できる() {
        DataCollection<Integer> collection = newTestData();

        assertEquals((int) collection.findWithSort(mFilter, 2), 4);
    }

}