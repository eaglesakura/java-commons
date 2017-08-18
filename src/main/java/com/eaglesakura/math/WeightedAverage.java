package com.eaglesakura.math;

import com.eaglesakura.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 加重平均を管理する
 */
public class WeightedAverage {
    private final int mCacheNum;

    private final double mDecay;

    private final List<Double> mValues = new ArrayList<>();

    public WeightedAverage(int cacheNum, double decay) {
        if (cacheNum < 1) {
            throw new IllegalStateException("CacheNum :: " + cacheNum);
        }
        mCacheNum = cacheNum;
        mDecay = decay;
    }

    /**
     * 加重平均した値を取得する
     */
    public double get() {
        return MathUtil.weightedAverage(mValues, mDecay);
    }

    /**
     * 新たに値を追加する
     * キャッシュ数が既定を超えた場合、古い値は捨てられる
     *
     * @param value 新しい値
     */
    public void put(double value) {
        mValues.add(0, value);
        while (mValues.size() > mCacheNum) {
            mValues.remove(mValues.size() - 1);
        }
    }

    /**
     * 値が1つも無ければtrue
     */
    public boolean empty() {
        return mValues.isEmpty();
    }

    /**
     * コンストラクタで指定されたキャッシュ数が満たされていればtrue
     */
    public boolean full() {
        return mValues.size() == mCacheNum;
    }

    /**
     * キャッシュをクリアする
     */
    public void clear() {
        mValues.clear();
    }
}
