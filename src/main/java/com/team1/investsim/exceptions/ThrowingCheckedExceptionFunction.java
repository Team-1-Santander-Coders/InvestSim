package com.team1.investsim.exceptions;

@FunctionalInterface
public interface ThrowingCheckedExceptionFunction<T, R> {
    R apply(T t) throws Exception;
}
