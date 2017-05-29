package com.eaglesakura.io;

import com.eaglesakura.lambda.CancelCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

/**
 * キャンセル動作に対応したInputStream
 */
public class CancelableInputStream extends InputStream {
    final InputStream mStream;

    /**
     * バッファサイズ
     */
    int mBufferSize = 1024 * 2;

    final CancelCallback mCancelCallback;

    public CancelableInputStream(InputStream stream, CancelCallback cancelCallback) {
        mStream = stream;
        mCancelCallback = cancelCallback;
        if (mStream == null || mCancelCallback == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 一度に読み込むバッファサイズを指定する
     */
    public void setBufferSize(int bufferSize) {
        mBufferSize = bufferSize;
        if (mBufferSize <= 0) {
            throw new IllegalArgumentException();
        }
    }

    private void throwIfCanceled() throws IOException {
        try {
            if (mCancelCallback.isCanceled()) {
                throw new InterruptedIOException();
            }
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read() throws IOException {
        byte[] buf = new byte[1];
        if (read(buf, 0, 1) < 0) {
            return -1;
        }
        return ((int) buf[0]) & 0x000000FF;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        throwIfCanceled();

        // キャンセルチェックを容易にするため、一度の取得を小さく保つ
        return mStream.read(buffer, byteOffset, Math.min(mBufferSize, byteCount));
    }

    @Override
    public long skip(long n) throws IOException {
        return mStream.skip(n);
    }

    @Override
    public int available() throws IOException {
        return mStream.available();
    }

    @Override
    public void close() throws IOException {
        mStream.close();
    }

    @Override
    public void mark(int readlimit) {
        mStream.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        mStream.reset();
    }

    @Override
    public boolean markSupported() {
        return mStream.markSupported();
    }
}
