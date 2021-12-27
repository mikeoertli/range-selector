package com.mikeoertli.rangeselector.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataUtilities
{
    private DataUtilities()
    {
        // prevent instantiation
    }

    public static List<Integer> buildRandomDataSet(int numDataPoints, int maxValue)
    {
        List<Integer> dataSet = new ArrayList<>();

        for (int i = 0; i < numDataPoints; i++)
        {
            dataSet.add(ThreadLocalRandom.current().nextInt(0, maxValue));
        }

        return dataSet;
    }
}
