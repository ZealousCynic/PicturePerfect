package com.example.pictureperfect.Model;

import com.example.pictureperfect.Model.Abstractions.IBitmap;
import com.example.pictureperfect.Model.Abstractions.IColorCalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColorCalculator implements IColorCalculator {

    private IBitmap toSearch;

    public IBitmap getBitmap() {return toSearch;}
    public void setBitmap(IBitmap bitmap) { toSearch = bitmap;}

    public ColorCalculator(IBitmap bitmap) {
        toSearch = bitmap;
    }

    @Override
    public int getDominantColor() {

        Map hashmap = getHashMap();

        int[] mostFrequent = findMostFrequent(hashmap);

        int toRet = createColorInt(mostFrequent);

        return toRet;
    }

    @Override
    public int[] getDominantColors(int amount) {

        Map hashmap = getHashMap();

        int[][] mostFrequent = new int[amount][3];

        //amount is also used as a counter -- instantiate arrays early
        int[] toRet = new int[amount];

        while(amount > 0) {
            mostFrequent[amount - 1] = findMostFrequent(hashmap);
            removeMostFrequent(hashmap, mostFrequent[amount - 1]);
            amount--;
        }

        int color;

        for(int i = toRet.length; i > 0; i--) {

            color  = createColorInt(mostFrequent[amount]);

            toRet[i - 1] = color;
            amount++;
        }

        return toRet;
    }

    Map<int[], Integer> getHashMap() {

        int[] pixels = getBitmapPixels(toSearch);

        int[][] rgbArrays;
        rgbArrays = new int[pixels.length][3];

        for(int i = 0; i < pixels.length; i++)
            rgbArrays[i] = getRGBFromPixel(pixels[i]);

        Map hashmap = mapRGBColors(rgbArrays);

        return hashmap;
    }

    private int[] findMostFrequent(Map<int[], Integer> hashmap) {

        int[] res = new int[] {-1,-1,-1};
        int highest_frequency = 0;

        for(Map.Entry<int[], Integer> val : hashmap.entrySet())
        {
            if(highest_frequency < val.getValue()){
                res = val.getKey();
                highest_frequency = val.getValue();
            }
        }

        return res;
    }

    void removeMostFrequent(Map<int[], Integer> hashmap, int[] key) {

        Set keyset = new HashSet();
        keyset.add(key);

        hashmap.keySet().removeAll(keyset);
    }

    private Map<int[], Integer> mapRGBColors(int[][] rgbArrays) {
        Map<int[], Integer> hashmap = new HashMap<int[], Integer>();

        for(int i = 0; i <rgbArrays.length; i++)
        {
            int[] key = rgbArrays[i];
            if(hashmap.containsKey(key)) {
                int frequency = hashmap.get(key);
                frequency++;
                hashmap.put(key, frequency);
            }
            else
                hashmap.put(key, 1);
        }

        return hashmap;
    }

    private int[] getRGBFromPixel(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = (pixel) & 0xff;

        return new int[]{r,g,b};
    }

    private int createColorInt(int[] toConvert) {
        return 0xff000000 | (toConvert[0] << 16) | (toConvert[1] << 8) | toConvert[2];
    }

    private int[] getBitmapPixels(IBitmap bitmap) {
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
}
