package com.lib.test;

/**
 * Created by chenaxing on 2017/8/17.
 */

public class CurrentThread extends Thread
{
    public CurrentThread()
    {
        System.out.println("CurrentThread -- begin");
        System.out.println("Thread.currentThread.getName() = " + Thread.currentThread().getName());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("CurrentThread -- end");
    }

    @Override
    public void run()
    {
        super.run();
        System.out.println("run function -- begin");
        System.out.println("current running thread name = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread.getName() = " + Thread.currentThread().getName());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("run function -- end");
    }
}
