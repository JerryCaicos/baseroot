package com.lib.test.chapter_one;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by JerryCaicos on 2017/8/17.
 */

public class MyThread extends Thread
{
    public static final String TAG = "MyThread";

    @Override
    public void run()
    {
        super.run();
        System.out.println(TAG);
        try
        {
            for(int i = 0; i < 10; i++)
            {
                int time = (int) (Math.random()*1000);
                Thread.sleep(time);
                System.out.println("run = " + Thread.currentThread().getName());
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
