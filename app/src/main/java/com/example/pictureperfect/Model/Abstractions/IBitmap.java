package com.example.pictureperfect.Model.Abstractions;

public interface IBitmap {
    int getWidth();
    int getHeight();
    int[] getPixels(final int offset, final int stride, final int x, final int y, final int width, final int height);
}
