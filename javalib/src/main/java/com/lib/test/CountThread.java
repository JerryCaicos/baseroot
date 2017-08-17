package com.lib.test;

/**
 * Created by chenaxing on 2017/8/17.
 */

public class CountThread extends Thread
{
    private int count = 5;
    @Override
    public void run()
    {
        super.run();
        System.out.println("count = " + (count--) + " , thread name = " + Thread.currentThread().getName());
    }
}
