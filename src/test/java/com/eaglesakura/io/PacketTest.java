package com.eaglesakura.io;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class PacketTest {


    @Test(expected = IOException.class)
    public void ファイルマジックが壊れた場合シリアライズに失敗する() throws Exception {
        byte[] origin = "this-is-test-data".getBytes();

        byte[] packed = Packet.encode(origin);
        assertNotNull(packed);

        packed[1]--;    // マジックを壊す

        new Packet().unpack(packed); // ここで例外が投げられる

        // ここに到達したら失敗
        fail();
    }

    @Test(expected = IOException.class)
    public void ベリファイデータが壊れた場合シリアライズに失敗する() throws Exception {
        byte[] origin = "this-is-test-data".getBytes();

        byte[] packed = Packet.decode(origin);
        assertNotNull(packed);

        packed[packed.length - 1]--;    // ベリファイコードを壊す

        new Packet().unpack(packed); // ここで例外が投げられる

        // ここに到達したら失敗
        fail();
    }
}
