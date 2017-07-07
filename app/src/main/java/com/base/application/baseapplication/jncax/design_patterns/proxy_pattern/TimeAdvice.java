package com.base.application.baseapplication.jncax.design_patterns.proxy_pattern;

import com.base.application.baseapplication.jncax.design_patterns.prototype_pattern.Address;

/**
 * Created by chenaxing on 2017/5/10.
 */

public class TimeAdvice implements Advice
{
    private long startTime;

    private long endTime;

    @Override
    public void before()
    {
        startTime = System.nanoTime();
    }

    @Override
    public void after()
    {
        endTime = System.nanoTime();
        System.out.printf("program calculate time is " + (endTime - startTime));
    }
}
