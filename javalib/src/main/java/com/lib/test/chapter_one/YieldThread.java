package com.lib.test.chapter_one;

/**
 * Created by JerryCaicos on 2017/9/4.
 */

public class YieldThread extends Thread
{
    @Override
    public void run()
    {
        super.run();
        long begin = System.currentTimeMillis();
        int count = 0;
        for(int i = 0; i < 500000; i++)
        {
            Thread.yield();
            count += i + 1;
        }
        long end = System.currentTimeMillis();
        System.out.println("run time " + (end - begin));

    }
}
