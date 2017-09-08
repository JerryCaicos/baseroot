package com.lib.test.chapter_one;

/**
 * Created by JerryCaicos on 2017/8/17.
 */

public class IsAliveThread extends Thread
{
    public IsAliveThread()
    {
        System.out.println("IsAliveThread status begin = " + this.isAlive());
    }

    @Override
    public void run()
    {
        super.run();
        System.out.println("IsAliveThread status running = " + this.isAlive());
    }
}
