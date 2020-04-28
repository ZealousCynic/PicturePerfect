package com.example.pictureperfect;

import android.graphics.Bitmap;

import com.example.pictureperfect.Model.Abstractions.IBitmap;

import java.util.concurrent.ExecutionException;

public interface PictureContracts {
    public interface PicturePresenter {
        int getDominantColor(IBitmap bitmap) throws ExecutionException, InterruptedException;
        int[] getDominantColors(IBitmap bitmap, int amount) throws ExecutionException, InterruptedException;
        int[] getDominantColors(IBitmap bitmap) throws ExecutionException, InterruptedException;
    }

    public interface PictureView {
        void updateDominantColorText(int color);
        void updateDominantColor(int color);

        void updateDominantColorsText(int[] colors);
        void updateDominantColors(int[] colors);
    }
}
