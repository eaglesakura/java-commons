package com.eaglesakura.util;

/**
 * Runnableをラップし、例外と戻り値を利用できるようにする。
 */
public class ThrowableRunner<ResultType, ErrorType extends Exception> implements Runnable {
    /**
     * 投げられた例外
     */
    private ErrorType mError;

    /**
     * 処理の戻り値
     */
    private ResultType mResult;

    private ThrowableRunnable<ResultType, ErrorType> mRunner;

    public ThrowableRunner(ThrowableRunnable<ResultType, ErrorType> runner) {
        mRunner = runner;
    }

    /**
     * 処理を実行させる
     */
    @Override
    public final void run() {
        try {
            mResult = mRunner.run();
        } catch (Exception e) {
            mError = (ErrorType) e;
        }
    }

    /**
     * 値を取得するか例外を投げる
     */
    public ResultType await() throws ErrorType {
        while (mResult == null) {
            if (mError != null) {
                throw mError;
            }

            Util.sleep(1);
        }
        return mResult;
    }

    /**
     * 値を取得できるか例外が投げられるまで待つ
     */
    public ResultType safeAwait() {
        try {
            return await();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasError() {
        return mError != null;
    }

    public ErrorType getError() {
        return mError;
    }

    public ResultType getResult() {
        return mResult;
    }

    public boolean hasResult() {
        return mResult != null;
    }

    /**
     * 値を取得するか例外を投げる
     */
    public ResultType getOrThrow() throws ErrorType {
        if (mError != null) {
            throw mError;
        }
        return mResult;
    }

    /**
     * インスタンスを生成する
     *
     * @param runner       実行アクション
     * @param <ResultType> 戻り値
     * @param <ErrorType>  エラー
     */
    public static <ResultType, ErrorType extends Exception> ThrowableRunner<ResultType, ErrorType> newInstance(ThrowableRunnable<ResultType, ErrorType> runner) {
        return new ThrowableRunner<>(runner);
    }
}
