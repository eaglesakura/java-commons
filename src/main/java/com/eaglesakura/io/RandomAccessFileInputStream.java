package com.eaglesakura.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * RandomAccessFileをInputStreamから利用する。
 */
public class RandomAccessFileInputStream extends InputStream {
    private RandomAccessFile mFile = null;

    /**
     * 読み込みオフセット
     */
    private int mOffset = 0;

    /**
     * ファイル全体のサイズ
     */
    private int mSize = 0;

    /**
     *
     * @param raf
     */
    public RandomAccessFileInputStream(RandomAccessFile raf) {
        mFile = raf;
        try {
            mSize = (int) mFile.length();
        } catch (Exception e) {

        }
    }

    /**
     *
     * @param raf
     */
    public RandomAccessFileInputStream(RandomAccessFile raf, int offset, int length) {
        mFile = raf;
        this.mOffset = offset;
        this.mSize = length;
    }

    @Override
    public int read() throws IOException {
        return mFile.read();

    }

    @Override
    public int read(byte[] b) throws IOException {
        return mFile.read(b);
    }

    @Override
    public int read(byte[] b, int offset, int length) throws IOException {
        return mFile.read(b, offset, length);
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readlimit) {
    }

    @Override
    public int available() throws IOException {
        // return 1024 * 200;
        int result = (int) (mSize - (mFile.getFilePointer() - mOffset));
        return result;
    }

    @Override
    public long skip(long byteCount) throws IOException {

        long start = mFile.getFilePointer();
        mFile.skipBytes((int) byteCount);

        return mFile.getFilePointer() - start;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public synchronized void reset() throws IOException {
        mFile.seek(mOffset);
    }
}
