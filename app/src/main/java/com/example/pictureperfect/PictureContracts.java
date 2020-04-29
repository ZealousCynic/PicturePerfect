package com.example.pictureperfect;

import android.graphics.Bitmap;

import com.example.pictureperfect.Model.Abstractions.IBitmap;

import java.util.concurrent.ExecutionException;

public interface PictureContracts {
    public interface PicturePresenter {
        int getDominantColor(IBitmap bitmap) throws ExecutionException, InterruptedException;
        void getDominantColors(IBitmap bitmap, int amount);
        void getDominantColors(IBitmap bitmap);
    }

    public interface PictureView {

        void updateDominantColorsText(int[] colors);
        void updateDominantColors(int[] colors);
    }
}
