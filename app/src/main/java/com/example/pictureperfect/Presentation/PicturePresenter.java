package com.example.pictureperfect.Presentation;

import com.example.pictureperfect.Model.Abstractions.IBitmap;
import com.example.pictureperfect.Model.CallableColorCalculator;
import com.example.pictureperfect.Model.CallableColorCalculatorMultiple;
import com.example.pictureperfect.Model.ColorCalculator;
import com.example.pictureperfect.PictureContracts;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PicturePresenter implements PictureContracts.PicturePresenter {

    private final PictureContracts.PictureView view;

    public PicturePresenter(PictureContracts.PictureView view) {
        this.view = view;

        if(this.view == null)
            return;
    }

    @Override
    public int getDominantColor(IBitmap bitmap) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> result = executor.submit(new CallableColorCalculator(new ColorCalculator(bitmap)));

        int res = result.get();

        return res;
    }

    @Override
    public int[] getDominantColors(IBitmap bitmap, int amount) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<int[]> result = executor.submit(new CallableColorCalculatorMultiple(new ColorCalculator(bitmap), amount));

        int[] res = result.get();

        return res;
    }

    @Override
    public int[] getDominantColors(IBitmap bitmap) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<int[]> result = executor.submit(new CallableColorCalculatorMultiple(new ColorCalculator(bitmap), 5));

        int[] res = result.get();

        return res;
    }


}
