package com.example.pictureperfect.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GetHashMapRunnable implements Runnable {

    ColorMapper mapper;

    final ColorCalculator _creator;
    ConcurrentHashMap<Integer, Integer> _hashmap;
    List<Integer> batch;

    public GetHashMapRunnable(final ColorCalculator creator) {_creator = creator;}
    public GetHashMapRunnable(final ColorCalculator creator, final ConcurrentHashMap<Integer, Integer> hashmap) {_creator = creator; _hashmap = hashmap; mapper = new ColorMapper();}
    public GetHashMapRunnable(final ColorCalculator creator, final ConcurrentHashMap<Integer, Integer> hashmap, List<Integer> firstBatch) {_creator = creator; _hashmap = hashmap; batch = firstBatch; mapper = new ColorMapper();}

    @Override
    public void run() {
        while(_creator.currentBatch < _creator.pixellist.size())
        {
            Integer[] arrToMap = batch.toArray(new Integer[batch.size()]);

            mapper.mapIntegerColors(arrToMap, _hashmap);

            int next = _creator.getNextBatch();

            if(next == -1 || next < _creator.pixellist.size()) {
                break;
            }
            List<Integer> batch = _creator.getNextFromList(next);
        }
    }
}
