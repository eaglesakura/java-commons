package com.eaglesakura.collection;

import com.eaglesakura.lambda.Action1;
import com.eaglesakura.lambda.Action2;
import com.eaglesakura.lambda.Matcher1;
import com.eaglesakura.lambda.ResultAction1;
import com.eaglesakura.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * データの集合を示すUtilクラス
 *
 * 元データをListとして持つため、速度やメモリ的には不利だが、簡単にフィルタリング等を行える。
 */
public class DataCollection<T> {
    final List<T> mDataList;

    Comparator<T> mComparator;

    public DataCollection(List<T> dataList) {
        mDataList = dataList;
    }

    public DataCollection() {
        mDataList = new ArrayList<>();
    }

    public DataCollection<T> setComparator(Comparator<T> comparator) {
        mComparator = comparator;
        return this;
    }

    public int size() {
        return mDataList.size();
    }

    public boolean isEmpty() {
        return mDataList.isEmpty();
    }

    /**
     * 全てのデータに対して処理を行う
     *
     * @param action 処理内容
     * @throws Exception action内部で投げられた例外
     */
    public void each(Action1<T> action) throws Exception {
        for (T data : mDataList) {
            action.action(data);
        }
    }


    /**
     * インデックス付きでデータ処理を行う
     */
    public void each(Action2<Integer, T> action) throws Exception {
        int index = 0;
        for (T data : mDataList) {
            action.action(index, data);
            ++index;
        }
    }

    /**
     * 全てのデータに対して処理を行う
     *
     * @param action 処理内容
     */
    public void safeEach(Action1<T> action) {
        try {
            for (T data : mDataList) {
                action.action(data);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * インデックス付きでデータ処理を行う
     */
    public void safeEach(Action2<Integer, T> action) {
        try {
            int index = 0;
            for (T data : mDataList) {
                action.action(index, data);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ソート後にデータに対して処理する
     *
     * @param action アクション
     * @throws Exception action内部の例外
     */
    public void sortEach(Action1<T> action) throws Exception {
        for (T data : list()) {
            action.action(data);
        }
    }

    /**
     * ソート後にデータに対して処理する
     *
     * @param action アクション
     * @throws Throwable action内部の例外
     */
    public void sortEach(Action2<Integer, T> action) throws Exception {
        int index = 0;
        for (T data : list()) {
            action.action(index, data);
            ++index;
        }
    }

    /**
     * ソート後にデータに対して処理する
     *
     * @param action アクション
     * @throws Throwable action内部の例外
     */
    public void sortSafeEach(Action2<Integer, T> action) {
        try {
            int index = 0;
            for (T data : list()) {
                action.action(index, data);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * データをMapに変換する
     *
     * @param filter     データフィルタ
     * @param keyConvert Key変換
     * @param <Key>      Keyクラス
     */
    public <Key> Map<Key, T> asMap(Matcher1<T> filter, ResultAction1<T, Key> keyConvert) {
        return CollectionUtil.asMap(list(filter, false), keyConvert);
    }

    /**
     * return用の新しいListを生成する
     */
    protected List<T> newList(int size) {
        return new ArrayList<T>(size);
    }

    /**
     * データソートを行う
     */
    protected void sort(List<T> items) {
        if (mComparator != null) {
            Collections.sort(items, mComparator);
        }
    }

    /**
     * 元のリストの参照をそのまま取得する
     */
    public List<T> getSource() {
        return mDataList;
    }

    /**
     * 全てのデータを取得する
     */
    public List<T> list() {
        return list(new Matcher1<T>() {
            @Override
            public boolean match(T it) throws Exception {
                return true;
            }
        }, true);
    }

    /**
     * 条件にマッチするアイテムを検索する
     *
     * MEMO: これはソースデータの順番で検索する
     *
     * @param filter フィルタ関数
     * @return 見つけた場合はそのオブジェクト、それ以外はnull
     */
    public T find(Matcher1<T> filter) {
        return find(filter, 0);
    }

    /**
     * 条件にマッチするアイテムを検索する
     *
     * MEMO: これはソースデータの順番で検索する
     *
     * @param filter フィルタ関数
     * @param n      N番目にヒットしたオブジェクトを検索する(最初にヒットしたオブジェクトは0, 以降1, 2, 3, 4とインクリメント
     * @return 見つけた場合はそのオブジェクト、それ以外はnull
     */
    public T find(Matcher1<T> filter, final int n) {
        try {
            int index = 0;
            for (T it : mDataList) {
                if (filter.match(it)) {
                    if (index == n) {
                        return it;
                    } else {
                        ++index;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 事前にデータをソートし、条件にマッチするアイテムを検索する
     *
     * @param filter フィルタ関数
     * @return 見つけた場合はそのオブジェクト、それ以外はnull
     */
    public T findWithSort(Matcher1<T> filter) {
        try {
            for (T it : list()) {
                if (filter.match(it)) {
                    return it;
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 事前にデータをソートし、条件にマッチするアイテムを検索する
     *
     * @param filter フィルタ関数
     * @param n      N番目にヒットしたオブジェクトを検索する(最初にヒットしたオブジェクトは0, 以降1, 2, 3, 4とインクリメント
     * @return 見つけた場合はそのオブジェクト、それ以外はnull
     */
    public T findWithSort(Matcher1<T> filter, int n) {
        try {
            int index = 0;
            for (T it : list()) {
                if (filter.match(it)) {
                    if (index == n) {
                        return it;
                    } else {
                        ++index;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 条件にマッチするデータを取得する
     */
    public List<T> list(Matcher1<T> filter) {
        return list(filter, true);
    }

    /**
     * 条件にマッチするデータを取得する
     */
    public List<T> list(Matcher1<T> filter, boolean sort) {
        try {
            List<T> result = newList(mDataList.size());
            for (T item : mDataList) {
                if (filter.match(item)) {
                    result.add(item);
                }
            }
            if (sort) {
                sort(result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
