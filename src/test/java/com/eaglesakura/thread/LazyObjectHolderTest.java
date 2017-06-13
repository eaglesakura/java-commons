package com.eaglesakura.thread;

import com.eaglesakura.lambda.ResultAction0;
import com.eaglesakura.lambda.ResultAction1;
import com.eaglesakura.lambda.ResultAction2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class LazyObjectHolderTest {

    @Test
    public void 遅延初期化が一度しか行われない() throws Throwable {
        LazyObjectHolder0<Object> holder0 = new LazyObjectHolder0<>(new ResultAction0<Object>() {
            @Override
            public Object action() throws Exception {
                return new Object();
            }
        });

        LazyObjectHolder1<Object, Object> holder1 = new LazyObjectHolder1<>(new ResultAction1<Object, Object>() {
            @Override
            public Object action(Object it) throws Exception {
                assertNotNull(it);
                return new Object();
            }
        });

        LazyObjectHolder2<Object, Object, Object> holder2 = new LazyObjectHolder2<>(new ResultAction2<Object, Object, Object>() {
            @Override
            public Object action(Object a, Object b) throws Exception {
                assertNotNull(a);
                assertNotNull(b);
                assertNotEquals(a, b);
                return new Object();
            }
        });

        assertNotNull(holder0.get());
        assertNotNull(holder1.get(new Object()));
        assertNotNull(holder2.get(new Object(), new Object()));

        assertEquals(holder0.get(), holder0.get());
        assertEquals(holder1.get(null), holder1.get(null));
        assertEquals(holder2.get(null, null), holder2.get(null, null));
    }
}