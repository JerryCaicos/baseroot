package com.lib.test;

/**
 * Created by JerryCaicos on 2017/8/30.
 */

public class InterrputThread extends Thread
{
    @Override
    public void run()
    {
        super.run();
        try
        {
            for(int i = 0; i < 100000; i++)
            {
                System.out.println("i = " + (i + 1));
                if(i % 100 == 0)
                {
                    Thread.sleep(80);
                }
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("this.isInterrupt() = " + this.isInterrupted());
            e.printStackTrace();
        }
    }
}
