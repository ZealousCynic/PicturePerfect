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

        Integer mostFrequent = findMostFrequent(hashmap);

        return mostFrequent;
    }

    @Override
    public int[] getDominantColors(int amount) {

        Map hashmap = getHashMap();

        Integer[] mostFrequent = new Integer[amount];
        int[] toRet = new int[amount];

        while(amount > 0) {
            mostFrequent[amount - 1] = findMostFrequent(hashmap);
            removeMostFrequent(hashmap, mostFrequent[amount - 1]);
            amount--;
        }

        for(int i = 0; i < toRet.length; i++)
            toRet[i] = mostFrequent[i];

        return toRet;
    }

    Map<int[], Integer> getHashMap() {

        int[] pixels = getBitmapPixels(toSearch);

        Map hashmap = mapColors(pixels);

        return hashmap;
    }

    private Integer findMostFrequent(Map<Integer, Integer> hashmap) {

        Integer res = -1;
        int highest_frequency = 0;

        for(Map.Entry<Integer, Integer> val : hashmap.entrySet())
        {
            if(highest_frequency < val.getValue()){
                res = val.getKey();
                highest_frequency = val.getValue();
            }
        }

        return res;
    }

    void removeMostFrequent(Map<Integer, Integer> hashmap, Integer key) {

        Set keyset = new HashSet();
        keyset.add(key);

        hashmap.keySet().removeAll(keyset);
    }

    private Map<Integer, Integer> mapColors(int[] colorArray) {
        Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();

        for(int i = 0; i <colorArray.length; i++)
        {
            Integer key = colorArray[i];
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
