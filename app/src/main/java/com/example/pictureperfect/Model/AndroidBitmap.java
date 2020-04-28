package com.example.pictureperfect.Model;

import android.graphics.Bitmap;

import com.example.pictureperfect.Model.Abstractions.IBitmap;

public class AndroidBitmap implements IBitmap {

    private final Bitmap _bitmap;

    public AndroidBitmap(Bitmap bitmap) { _bitmap = bitmap; }

    @Override
    public int getWidth() {
        return _bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return _bitmap.getHeight();
    }

    @Override
    public int[] getPixels(int offset, int stride, int x, int y, int width, int height) {
        int[] toRet = new int[width * height];

        _bitmap.getPixels(toRet, offset,stride,x,y,width,height);

        return toRet;
    }
}
