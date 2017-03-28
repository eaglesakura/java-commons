package com.eaglesakura.io;

import com.eaglesakura.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Byte配列にベリファイコードを付与する。
 *
 * UDP等、信頼できない端末間通信等で扱う小さなデータをチェックサムとヘッダを付与して保護する。
 * デフォルトでは非常に小さなデータ（最大で1kb前後）を想定するため、ベリファイコード自体も少量のみとなる。
 */
public class Packet {
    private static final byte[] MAGIC = new byte[]{
            3, 1, 0, 3
    };

    /**
     * ヘッダに付与するマジックナンバーを取得する
     */
    protected byte[] getMagicNumber() {
        return MAGIC;
    }

    /**
     * チェックサムを計算する
     */
    protected byte[] getCheckSum(byte[] src) {
        final int offset = 0;
        final int length = src.length;

        short sum0 = (short) src.length;
        short sum1 = 0;
        {
            // すべての配列を加算する
            for (int i = 0; i < length; ++i) {
                sum0 += src[offset + i];
                sum0 = (short) ((sum0 << 1) | (sum0 >> 15 & 1)); // bit rotate
            }
        }
        {
            // すべての配列を乗算する
            int temp = (src.length) | 0x01;
            for (int i = 0; i < length; ++i) {
                temp *= (((int) src[offset + i]) | 0x01);
            }
            sum1 = (short) temp;
        }

        return new byte[]{
                (byte) (sum0 >> 8 | 0x04),
                (byte) (sum0 | 0x08),
                (byte) (sum1 >> 8 | 0x01),
                (byte) (sum1 | 0x02),
        };
    }

    /**
     * データにチェックサムを付与し、破壊や改ざんのチェックを行えるようにする
     *
     * @param src 元データ
     * @return チェックサムが付与されたデータ
     */
    protected byte[] pack(byte[] src) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        // マジックナンバー
        {
            byte[] number = getMagicNumber();
            dos.writeBuffer(number, 0, number.length);
        }

        // データ本体
        dos.writeFile(src);

        // 検証コードを付与する
        {
            byte[] verify = getCheckSum(src);
            dos.writeBuffer(verify, 0, verify.length);
        }

        return os.toByteArray();
    }

    /**
     * pack()で処理されたデータから、元の情報を取得する。
     *
     * その際、データが壊れている場合は例外を投げる。
     *
     * @param packedData チェックサムが付与されたデータ
     * @return 元のデータ
     */
    protected byte[] unpack(byte[] packedData) throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packedData));
        byte[] originMagic = getMagicNumber();
        byte[] magic = dis.readBuffer(originMagic.length);

        if (!Arrays.equals(originMagic, magic)) {
            throw new UnsupportedEncodingException(StringUtil.format("Data Format Error %s != %s", StringUtil.toHexString(magic), StringUtil.toHexString(originMagic)));
        }

        byte[] file = dis.readFile();
        byte[] fileVerify = getCheckSum(file);
        byte[] verify = dis.readBuffer(fileVerify.length);

        if (!Arrays.equals(fileVerify, verify)) {
            final String fileVerifyHex = StringUtil.toHexString(fileVerify);
            final String verifyHex = StringUtil.toHexString(verify);

            throw new InvalidObjectException(StringUtil.format("Verify Error [%s] != [%s]", fileVerifyHex, verifyHex));
        }

        // ファイル本体を返す
        return file;
    }

    /**
     * バイナリデータをパケット情報に変換する。
     * パケットにはヘッダとチェックサムが付与される。
     *
     * @param src 元データ
     * @return ヘッダとチェックサムが付与されたデータ
     */
    public static byte[] encode(byte[] src) throws IOException {
        return new Packet().pack(src);
    }

    /**
     * パケットから元のバイナリを取り出す。
     * チェックサムが壊れている場合など、異常なデータの場合は例外を投げる。
     *
     * @param packet {@link #encode(byte[])}によって生成されたパケット情報
     * @return パケットに含まれていたバイナリ情報
     */
    public static byte[] decode(byte[] packet) throws IOException {
        return new Packet().unpack(packet);
    }
}
