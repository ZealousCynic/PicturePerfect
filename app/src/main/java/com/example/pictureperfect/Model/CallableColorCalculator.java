package com.example.pictureperfect.Model;

import java.util.concurrent.Callable;

public class CallableColorCalculator implements Callable<Integer> {

    final private ColorCalculator _calculator;

    public CallableColorCalculator(final ColorCalculator calculator) {
        _calculator = calculator;
    }

    @Override
    public Integer call() throws Exception {
        try {
            return _calculator.getDominantColor();
        }
        catch (Exception e) {
            throw e;
        }
    }
}
