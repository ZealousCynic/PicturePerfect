package com.example.pictureperfect.Model;

import com.example.pictureperfect.Model.Abstractions.IBitmap;
import com.example.pictureperfect.Model.Abstractions.IColorCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ColorCalculator implements IColorCalculator {

    private IBitmap toSearch;

    private BitMapper bmapper;
    private ColorMapper cmapper;

    int currentBatch;
    private int batchSize;

    int[] pixels;
    List<Integer> pixellist;
    Map<Integer, Integer> _hashmap;

    public ColorCalculator(IBitmap bitmap) {
        toSearch = bitmap;
        bmapper = new BitMapper();
        cmapper = new ColorMapper();
        _hashmap = new ConcurrentHashMap<Integer, Integer>();
    }

    @Override
    public int getDominantColor() throws ExecutionException, InterruptedException {

        Map hashmap = getHashMapThreaded();

        Integer mostFrequent = cmapper.getMostFrequent(hashmap);

        return mostFrequent;
    }

    @Override
    public int[] getDominantColors(int amount) throws ExecutionException, InterruptedException {

        getHashMapThreaded();

        Integer[] mostFrequent = new Integer[amount];
        int[] toRet = new int[amount];

        while(amount > 0) {
            mostFrequent[amount - 1] = cmapper.getMostFrequent(_hashmap);
            amount--;
        }

        for(int i = 0; i < toRet.length; i++)
            toRet[i] = mostFrequent[i];

        return toRet;
    }

    final Map<Integer, Integer> getHashMap() {
        pixels = bmapper.getBitmapPixels(toSearch);

        Map hashmap = cmapper.mapColors(pixels);

        return hashmap;
    }

    final Map<Integer, Integer> getHashMapThreaded() throws ExecutionException, InterruptedException {

        initialize();

        //THREADS

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future> futures = new ArrayList<Future>();

        futures.add(executor.submit(new GetHashMapRunnable(this, (ConcurrentHashMap)_hashmap,
                getNextFromList(0))));

        for(int i = 0; i < 3; i++)
        futures.add(executor.submit(new GetHashMapRunnable(this, (ConcurrentHashMap)_hashmap,
                getNextFromList(getNextBatch()))));

        try {
            for(Future f : futures)
                f.get();
        }catch(Throwable e) {
            throw e;
        }
        executor.shutdown();

        return _hashmap;
    }

    void initialize() {

        pixellist = Arrays.asList(bmapper.getIntegerBitmapPixels(toSearch));

        currentBatch = 0;
        batchSize = 1000;
    }

    synchronized int getNextBatch() {

        currentBatch += batchSize;
        if(currentBatch <= pixellist.size())
        {
            return currentBatch;
        }
        else
        {
            return -1;
        }
    }

    synchronized List<Integer> getNextFromList(int from) {
        return Collections.unmodifiableList(pixellist.subList(from, from + batchSize));
    }


    private int createColorInt(int[] toConvert) {
        return 0xff000000 | (toConvert[0] << 16) | (toConvert[1] << 8) | toConvert[2];
    }
}
