package com.eaglesakura.io;

import com.eaglesakura.util.IOUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 指定されたエンディアンでデータを書き込む。
 *
 * このクラスを通して出力したファイルは対になる {@link DataInputStream}で開くことが可能。
 */
public final class DataOutputStream implements Closeable {
    /**
     * 入出力。
     */
    private OutputStream mOutput;


    public DataOutputStream(OutputStream os) {
        mOutput = os;
    }

    /**
     * リソースの開放を行う。
     */
    @Override
    public void close() {
        IOUtil.close(mOutput);
        mOutput = null;
    }

    /**
     * 実際のバッファへ書き込みを行う。
     *
     * @param buf      書き込み対象のバッファ
     * @param position buf[position]
     * @param length   書き込みbyte数
     */
    public void writeBuffer(byte[] buf, int position, int length) throws IOException {
        mOutput.write(buf, position, length);
    }

    /**
     * 1バイト整数を保存する。
     */
    public void writeS8(byte n) throws IOException {
        byte[] buf = {
                n,
        };
        writeBuffer(buf, 0, buf.length);
    }

    /**
     * boolとして書き込む
     */
    public void writeBoolean(boolean b) throws IOException {
        writeS8(b ? (byte) 1 : (byte) 0);
    }

    /**
     * 2バイト整数を保存する。
     */
    public void writeS16(short n) throws IOException {
        byte[] buf = {
                (byte) ((((int) n) >> 8) & 0xff), (byte) ((((int) n) >> 0) & 0xff),
        };
        writeBuffer(buf, 0, buf.length);
    }

    /**
     * 4バイト整数を保存する。
     */
    public void writeS32(int n) throws IOException {
        byte[] buf = {
                (byte) ((((int) n) >> 24) & 0xff), (byte) ((((int) n) >> 16) & 0xff), (byte) ((((int) n) >> 8) & 0xff),
                (byte) ((((int) n) >> 0) & 0xff),
        };
        writeBuffer(buf, 0, buf.length);
    }

    /**
     * 8バイト整数を保存する。
     */
    public void writeS64(long n) throws IOException {
        byte[] buf = {

                (byte) ((n >> 56) & 0xff), //!
                (byte) ((n >> 48) & 0xff), //!
                (byte) ((n >> 40) & 0xff), //!
                (byte) ((n >> 32) & 0xff), //!

                (byte) ((n >> 24) & 0xff), //!
                (byte) ((n >> 16) & 0xff), //!
                (byte) ((n >> 8) & 0xff), //!
                (byte) ((n >> 0) & 0xff), //!
        };
        writeBuffer(buf, 0, buf.length);
    }

    /**
     * 4バイト整数の配列を保存する。
     */
    public final void writeS32Array(final int[] buffer) throws IOException {
        byte[] temp = new byte[buffer.length * 4];
        int ptr = 0;
        for (int n : buffer) {
            temp[ptr] = (byte) ((n >> 24) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 16) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 8) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 0) & 0xff);
            ptr++;
        }
        writeBuffer(temp, 0, temp.length);
    }

    /**
     * 4バイト整数の配列を保存する。
     */
    public final void writeS32ArrayWithLength(final int[] buffer) throws IOException {
        writeS32(buffer.length);
        byte[] temp = new byte[buffer.length * 4];
        int ptr = 0;
        for (int n : buffer) {
            temp[ptr] = (byte) ((n >> 24) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 16) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 8) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 0) & 0xff);
            ptr++;
        }
        writeBuffer(temp, 0, temp.length);
    }

    /**
     * 8バイト整数の配列を保存する。
     */
    public final void writeS64Array(final long[] buffer) throws IOException {
        final byte[] temp = new byte[buffer.length * 8];
        int ptr = 0;
        for (long n : buffer) {
            temp[ptr] = (byte) ((n >> 56) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 48) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 40) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 32) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 24) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 16) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 8) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 0) & 0xff);
            ptr++;
        }
        writeBuffer(temp, 0, temp.length);
    }

    /**
     * 8バイト整数の配列を保存する。
     */
    public final void writeS64ArrayWithLength(final long[] buffer) throws IOException {
        writeS32(buffer.length);
        final byte[] temp = new byte[buffer.length * 8];
        int ptr = 0;
        for (long n : buffer) {
            temp[ptr] = (byte) ((n >> 56) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 48) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 40) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 32) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 24) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 16) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 8) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 0) & 0xff);
            ptr++;
        }
        writeBuffer(temp, 0, temp.length);
    }

    /**
     * 浮動小数点配列を保存する。
     */
    public void writeFloatArray(float[] buffer) throws IOException {
        byte[] temp = new byte[buffer.length * 4];
        int ptr = 0;
        for (float f : buffer) {
            int n = Float.floatToIntBits(f);
            temp[ptr] = (byte) ((n >> 24) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 16) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 8) & 0xff);
            ptr++;
            temp[ptr] = (byte) ((n >> 0) & 0xff);
            ptr++;
        }
        writeBuffer(temp, 0, temp.length);
    }

    /**
     * 浮動小数値をGL形式の固定小数として保存する。
     * <br>
     * このメソッドは少数精度を著しく悪くすることに注意すること。
     */
    @Deprecated
    public void writeGLFloat(float f) throws IOException {
        int n = (int) (f * (float) 0x10000);

        byte[] buf = {
                (byte) ((((int) n) >> 24) & 0xff), (byte) ((((int) n) >> 16) & 0xff), (byte) ((((int) n) >> 8) & 0xff),
                (byte) ((((int) n) >> 0) & 0xff),
        };
        writeBuffer(buf, 0, buf.length);
    }

    /**
     * 浮動小数値を書き込む。
     */
    public void writeFloat(float f) throws IOException {
        writeS32(Float.floatToIntBits(f));
    }

    /**
     * 浮動小数値を書き込む。
     */
    public void writeDouble(double d) throws IOException {
        writeS64(Double.doubleToLongBits(d));
    }

    /**
     * 文字列を書き込む。
     * <br>
     * エンコードはUTF-8として保存する。
     */
    public void writeString(String str) throws IOException {
        byte[] buf = str.getBytes("UTF-8");
        //! 文字列の長さを保存。
        writeS16((short) buf.length);
        //! 文字列本体を保存。
        writeBuffer(buf, 0, buf.length);
    }

    /**
     * 書き込みを行った場合の保存バイト数を計算する。
     */
    public static int getWriteSize(String str) {
        try {
            byte[] buf = str.getBytes("UTF-8");
            return buf.length + 2;
        } catch (Exception e) {
            byte[] buf = str.getBytes();
            return buf.length + 2;
        }
    }

    /**
     * 配列の大きさと本体を保存する。
     * <br>
     * bufferがnullである場合、0バイトのファイルとして保存する。
     *
     * @param buffer 書き込むファイル
     */
    public void writeFile(byte[] buffer) throws IOException {
        if (buffer == null) {
            writeS32(0);
            return;
        }

        //! 配列の長さ
        writeS32(buffer.length);
        //! 配列本体
        writeBuffer(buffer, 0, buffer.length);
    }
}
