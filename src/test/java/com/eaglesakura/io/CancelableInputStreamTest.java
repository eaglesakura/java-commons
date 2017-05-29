package com.eaglesakura.io;

import com.eaglesakura.lambda.CancelCallback;

import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;

public class CancelableInputStreamTest {

    @Test
    public void 適当なデータをすべて読み取れる() throws Throwable {
        CancelableInputStream is = new CancelableInputStream(new ByteArrayInputStream(new byte[256]), new CancelCallback() {
            @Override
            public boolean isCanceled() throws Throwable {
                return false;
            }
        });

        assertEquals(is.read(new byte[512]), 256);
    }

    @Test
    public void ゼロbyteのデータを読み取れる() throws Throwable {
        CancelableInputStream is = new CancelableInputStream(new ByteArrayInputStream(new byte[0]), new CancelCallback() {
            @Override
            public boolean isCanceled() throws Throwable {
                return false;
            }
        });

        assertEquals(is.read(), -1);
    }
}