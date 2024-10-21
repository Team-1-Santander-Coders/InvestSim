package com.team1.investsim.utils;

import com.team1.investsim.exceptions.ThrowingCheckedExceptionFunction;
import java.util.function.Function;

public class ThrowingFunctionWrapper {
    public static <T, R> Function<T, R> wrap(ThrowingCheckedExceptionFunction<T, R> throwingFunction) {
        return item -> {
            try {
                return throwingFunction.apply(item);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
    }
}
