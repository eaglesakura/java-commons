package com.eaglesakura.lambda;

import com.eaglesakura.util.Timer;

public class CallbackUtils {

    /**
     * 指定時間だけウェイトをかける。
     * <p>
     * 途中でキャンセルされた場合は例外を投げて終了される
     */
    public static void waitTime(long timeMs, CancelCallback cancelCallback) throws InterruptedException {
        if (timeMs <= 0) {
            return;
        }

        Timer timer = new Timer();
        while (timer.end() < timeMs) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }

            if (isCanceled(cancelCallback)) {
                throw new InterruptedException();
            }
        }
    }

    public static boolean isCanceled(CancelCallback callback) {
        if (callback == null) {
            return false;
        }
        try {
            return callback.isCanceled();
        } catch (Exception e) {
            return true;
        }
    }
}
