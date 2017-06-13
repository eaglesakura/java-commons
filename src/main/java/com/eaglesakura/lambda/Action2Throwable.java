package com.eaglesakura.lambda;

public interface Action2Throwable<T, T2, E extends Exception> {
    void action(T it, T2 obj2) throws E;
}
