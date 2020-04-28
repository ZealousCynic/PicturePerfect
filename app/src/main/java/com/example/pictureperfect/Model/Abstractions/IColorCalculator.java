package com.example.pictureperfect.Model.Abstractions;

import android.graphics.Bitmap;

public interface IColorCalculator {
    int getDominantColor();
    int[] getDominantColors(int amount);
}
