package com.example.pictureperfect.Model.Abstractions;

import android.graphics.Bitmap;

import java.util.concurrent.ExecutionException;

public interface IColorCalculator {
    int getDominantColor() throws InterruptedException, ExecutionException;
    int[] getDominantColors(int amount) throws InterruptedException, ExecutionException;
}
