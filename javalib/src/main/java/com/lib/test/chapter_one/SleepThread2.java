package com.lib.test.chapter_one;

/**
 * Created by JerryCaicos on 2017/9/8.
 */

public class SleepThread2 extends Thread
{
    @Override
    public void run()
    {
        super.run();
        try
        {
            long start  = System.currentTimeMillis();
            System.out.println("running thread name = " + this.currentThread().getName()
                    + " begin time = " + start);
            Thread.sleep(2000);
            long end = System.currentTimeMillis();
            System.out.println("running thread name = " + this.currentThread().getName()
                    + " end time = " + end);
            System.out.println("end - start in thread run func = " + (end - start));
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}

