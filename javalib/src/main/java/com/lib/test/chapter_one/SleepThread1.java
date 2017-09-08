package com.lib.test.chapter_one;

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