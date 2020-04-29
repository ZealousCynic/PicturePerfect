package com.example.pictureperfect.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColorMapper {


    public final Map<Integer, Integer> mapColors(int[] colorArray) {

        final Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();

        for(int i = 0; i < colorArray.length; i++)
        {
            Integer key = colorArray[i];

            if(addCloseColor(hashmap, key))
                continue;

            hashmap.put(key, 1);
        }

        return hashmap;
    }

    public void mapIntegerColors(Integer[] colorArray, Map<Integer,Integer> hashmap) {

        for(int i = 0; i < colorArray.length; i++)
        {
            Integer key = colorArray[i];

            if(addCloseColor(hashmap, key))
                continue;

            hashmap.put(key, 1);
        }
    }

    public final Integer getMostFrequent(final Map<Integer, Integer> hashmap) {
        Integer toRet = findMostFrequent(hashmap);
        removeMostFrequent(hashmap, toRet);

        return toRet;
    }

    private boolean addCloseColor(final Map<Integer, Integer> hashmap, Integer key) {

        for(Map.Entry<Integer, Integer> val : hashmap.entrySet())
        {
            Integer comparison = val.getKey();

            int distance = getEuclideanDistance(key, comparison);

            if(distance < 20)
            {
                Integer frequency = val.getValue();
                frequency++;
                hashmap.put(comparison, frequency);

                return true;
            }
        }
        return false;
    }

    private Integer findMostFrequent(final Map<Integer, Integer> hashmap) {

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

    private void removeMostFrequent(Map<Integer, Integer> hashmap, Integer key) {

        Set keyset = new HashSet();
        keyset.add(key);

        hashmap.keySet().removeAll(keyset);
    }

    //Takes Integers but needs to convert them to rgb
    private int getEuclideanDistance(Integer colorA, Integer colorB) {
        int[] rgbA = createRGBArrayFromInteger(colorA);
        int[] rgbB = createRGBArrayFromInteger(colorB);

        //You could do all sorts of nifty cpu optimizations with these calculations.
        int distance = (int)Math.sqrt(Math.pow(rgbA[0] - rgbB[0], 2) + (Math.pow(rgbA[1] - rgbB[1], 2) + (Math.pow(rgbA[2] - rgbB[2], 2))));

        return distance;
    }
    private int[] createRGBArrayFromInteger(Integer toConvert) {
        return new int[] {
                (toConvert >> 16) & 0xff,
                (toConvert >> 8) & 0xff,
                toConvert & 0xff
        };
    }
}
