package com.lib.test;

/**
 * Created by JerryCaicos on 2017/8/18.
 */

public class SleepThread1 extends Thread
{
    @Override
    public void run()
    {
        super.run();
        try
        {
            System.out.println("running thread name = " + this.currentThread().getName() + " begin ");
            Thread.sleep(2000);
            System.out.println("running thread name = " + this.currentThread().getName() + " end ");
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}


class SleepThread2 extends Thread
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
