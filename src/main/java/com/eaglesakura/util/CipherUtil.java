package com.eaglesakura.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CipherUtil {

    private static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";

    /**
     * 平文を暗号化する
     *
     * @param buffer    暗号化するバッファ
     * @param encodeKey 暗号化キー
     * @return 暗号化されたバッファ
     */
    public static byte[] encode(byte[] buffer, Key encodeKey) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TYPE);

            cipher.init(Cipher.ENCRYPT_MODE, encodeKey);

            return cipher.doFinal(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 暗号化された文章を平文に直す
     *
     * @param buffer    暗号化されたバッファ
     * @param decodeKey 復号化するキー
     * @return 復号化されたバッファ
     */
    public static byte[] decode(byte[] buffer, Key decodeKey) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TYPE);

            cipher.init(Cipher.DECRYPT_MODE, decodeKey);

            return cipher.doFinal(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * ランダム要素でキーペアを作成する
     *
     * @return ランダムなキーペア
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);
            KeyPair keyPair = gen.generateKeyPair();
            return keyPair;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 事前に作成した秘密鍵を読み込む
     * <br>
     * $ openssl genrsa -out private_key.pem 2048
     * <br>
     * $ openssl pkcs8 -in private.pem -outform der -out private.p8.der -topk8 -nocrypt
     *
     * @param derFileBuffer consoleで生成された*.derファイルのバッファ
     * @return 秘密鍵
     */
    public static PrivateKey loadPrivateKey(byte[] derFileBuffer) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(derFileBuffer);

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 事前に作成した公開鍵を読み込む
     * <br>
     * $ openssl rsa -in private.pem -pubout -outform DER -out public.der
     *
     * @param derFileBuffer consoleで生成された*.derファイルのバッファ
     * @return 公開鍵
     */
    public static PublicKey loadPublicKey(byte[] derFileBuffer) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(derFileBuffer);

            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
