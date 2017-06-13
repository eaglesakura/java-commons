package com.eaglesakura.lambda;

public interface Action1Throwable<T, E extends Exception> {
    void action(T it) throws E;
}
