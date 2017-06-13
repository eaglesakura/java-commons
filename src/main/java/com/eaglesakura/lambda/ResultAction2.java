package com.eaglesakura.lambda;

public interface ResultAction2<T1, T2, R> {
    R action(T1 a, T2 b) throws Exception;
}
