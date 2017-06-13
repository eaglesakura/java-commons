package com.eaglesakura.lambda;

public interface ResultAction1<T, R> {
    R action(T it) throws Exception;
}
