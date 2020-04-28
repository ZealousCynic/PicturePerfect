package com.example.pictureperfect.Model;

import java.util.concurrent.Callable;

public class CallableColorCalculatorMultiple implements Callable {
    final private ColorCalculator _calculator;
    final private int _amount;

    public CallableColorCalculatorMultiple(final ColorCalculator calculator, int amount) {
        _calculator = calculator;
        _amount = amount;

    }

    @Override
    public int[] call() throws Exception {
        try {
            return _calculator.getDominantColors(_amount);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
