package com.example.pictureperfect.Model;

import com.example.pictureperfect.Model.Abstractions.IBitmap;

public class BitMapper {

    public int[] getBitmapPixels(IBitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Create an array that can hold all the pixels of a 2d bitmap
        int[] pixels = bitmap.getPixels(0,width,0,0,width,height);

        final int[] subsetPixels = new int[width * height];

        for (int row = 0; row < height; row++) {
            System.arraycopy(pixels, (row * bitmap.getWidth()),
                    subsetPixels, row * width, width);
        }

        return subsetPixels;
    }

    public Integer[] getIntegerBitmapPixels(IBitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Create an array that can hold all the pixels of a 2d bitmap
        int[] pixels = bitmap.getPixels(0,width,0,0,width,height);

        //final Integer[] subsetPixels = new Integer[width * height];
        final Integer[] toRet = new Integer[width * height];

        //for (int row = 0; row < height; row++) {
            //System.arraycopy(pixels, (row * bitmap.getWidth()),
            //        subsetPixels, row * width, width);
        //}
        for(int i = 0; i < toRet.length; i++)
            toRet[i] = pixels[i];
        return toRet;
    }
}
