package com.eaglesakura.util;

import com.eaglesakura.lambda.Action1;
import com.eaglesakura.lambda.Action2;
import com.eaglesakura.lambda.Matcher1;
import com.eaglesakura.lambda.ResultAction1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtil {

    /**
     * コレクションをすべてチェックし、trueを返した場合に削除を行う
     *
     * @param srcdst  入出力
     * @param execute 実行し、trueを返したオブジェクトについては削除する
     * @return 削除数
     */
    public static <T> int eachRemove(Collection<T> srcdst, Matcher1<T> execute) throws Throwable {
        int result = 0;
        Iterator<T> iterator = srcdst.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            if (execute.match(item)) {
                iterator.remove();
                ++result;
            }
        }
        return result;
    }

    /**
     * コレクションをすべてチェックし、trueを返した場合に削除を行う
     *
     * @param srcdst  入出力
     * @param execute 実行し、trueを返したオブジェクトについては削除する
     */
    public static <T> int safeEachRemove(Collection<T> srcdst, Matcher1<T> execute) {
        try {
            return eachRemove(srcdst, execute);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * データのフィルタリングを行う
     *
     * @param src    フィルタリング元のList
     * @param dst    フィルタリング先のList
     * @param filter フィルタ関数
     * @return dstオブジェクト
     */
    public static <T> List<T> filter(List<T> src, List<T> dst, Matcher1<T> filter) throws Throwable {
        for (T it : src) {
            if (filter.match(it)) {
                dst.add(it);
            }
        }
        return dst;
    }

    /**
     * 含まれているnullを削除し、参照を返す
     */
    public static <T> List<T> trimNull(List<T> srcDst) {
        Iterator<T> iterator = srcDst.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                iterator.remove();
            }
        }
        return srcDst;
    }

    /**
     * Setの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <T> Set<T> each(Set<T> set, Action1<T> action) throws Throwable {
        for (T it : set) {
            action.action(it);
        }
        return set;
    }

    /**
     * Mapの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <K, V> Map<K, V> each(Map<K, V> map, Action1<V> action) throws Throwable {
        for (V it : map.values()) {
            action.action(it);
        }
        return map;
    }

    /**
     * Mapの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <K, V> Map<K, V> each(Map<K, V> map, Action2<K, V> action) throws Throwable {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            action.action(entry.getKey(), entry.getValue());
        }
        return map;
    }

    /**
     * Listの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <T> List<T> each(List<T> list, Action1<T> action) throws Throwable {
        if (list == null || list.isEmpty()) {
            return list;
        }

        for (T it : list) {
            action.action(it);
        }
        return list;
    }

    /**
     * データのフィルタリングを行う
     *
     * 内部で例外が発生した場合、RuntimeExceptionとして投げる
     *
     * @param src    フィルタリング元のList
     * @param dst    フィルタリング先のList
     * @param filter フィルタ関数
     * @return dstオブジェクト
     */
    public static <T> List<T> safeFilter(List<T> src, List<T> dst, Matcher1<T> filter) {
        try {
            return filter(src, dst, filter);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Listの全オブジェクトに対して処理を行い、同一オブジェクトを返却する
     */
    public static <T> List<T> safeEach(List<T> list, Action1<T> action) {
        try {
            return each(list, action);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * itemが重複しないようにaddする
     */
    public static <T> boolean addUnique(List<T> list, T item) {
        if (!list.contains(item)) {
            list.add(item);
            return true;
        } else {
            return false;
        }
    }

    /**
     * アイテムを追加し、追加したインデックスを返す
     */
    public static <T> int addUniqueRequestIndex(List<T> list, T item) {
        addUnique(list, item);
        return list.indexOf(item);
    }

    /**
     * @see Collection#toArray(Object[])
     */
    public static <T> T[] asArray(Collection<T> c, T[] array) {
        return c.toArray(array);
    }

    /**
     * @param array 変換元配列
     * @param <T>   type
     * @return 変換したList
     */
    public static <T> List<T> asList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    /**
     * リストに全てのオブジェクトを登録し、listをそのまま返す
     */
    public static <T> List<T> addAll(List<T> list, Collection<T> items) {
        list.addAll(items);
        return list;
    }

    /**
     * 2つの配列をコピーする
     */
    public static <T> T[] copyOf(T[] array) {
        if (array == null) {
            return null;
        }
        return Arrays.copyOf(array, array.length);
    }

    /**
     * Mapに変換する
     *
     * @see CollectionUtil#
     */
    public static <Key, Value> Map<Key, Value> asMap(Collection<Value> values, ResultAction1<Value, Key> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        for (Value value : values) {
            try {
                result.put(keyCreator.action(value), value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 入力をKey-Valueの連続した配列として扱い、Mapに変換する
     */
    public static <Key, Value> Map<Key, Value> asPairMap(Collection<Value> values, ResultAction1<Value, Key> keyCreator) {
        Map<Key, Value> result = new HashMap<>();
        Iterator<Value> iterator = values.iterator();
        while (iterator.hasNext()) {
            try {
                Key key = keyCreator.action(iterator.next());
                Value value = iterator.next();
                result.put(key, value);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static <T, R> Set<R> asOtherSet(Iterable<T> origin, ResultAction1<T, R> converter) {
        Set<R> result = new HashSet<>();
        for (T item : origin) {
            try {
                result.add(converter.action(item));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


    /**
     * 別なクラスのリストへ変換する
     */
    public static <T, R> List<R> asOtherList(Iterable<T> origin, ResultAction1<T, R> converter) {
        List<R> result = new ArrayList<>();
        for (T item : origin) {
            try {
                result.add(converter.action(item));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 別なクラスのリストへ変換する
     */
    public static <T, R> List<R> asOtherList(Iterator<T> origin, ResultAction1<T, R> converter) {
        List<R> result = new ArrayList<>();
        while (origin.hasNext()) {
            try {
                result.add(converter.action(origin.next()));
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 指定したValueが設定されているKeyを探す。複数ある場合は最初に見つかったものを返却する。
     * 見つからない場合はnullを返却する。
     *
     * @param map   検索
     * @param value 検索対象の値
     * @param <K>   Key
     * @param <V>   Value
     */
    public static <K, V> K findKeyFromValue(Map<K, V> map, V value) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public static boolean isEmpty(byte[] item) {
        return item == null || item.length == 0;
    }

    public static <T> boolean isEmpty(T[] item) {
        return item == null || item.length == 0;
    }

    public static <T> boolean isEmpty(Collection<T> item) {
        return item == null || item.isEmpty();
    }


    /**
     * 全てが!=nullであればtrue
     */
    public static boolean allNotNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }
}
