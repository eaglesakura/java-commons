package com.eaglesakura.lambda;

/**
 * キャンセルチェックを行う
 */
public interface CancelCallback {
    boolean isCanceled() throws Exception;
}
