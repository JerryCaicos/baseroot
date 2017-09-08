package com.lib.test.chapter_one;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

/**
 * Created by JerryCaicos on 2017/9/1.
 */

public class ReturnInterruptThread extends Thread
{
    @Override
    public void run()
    {
        super.run();
        while(true)
        {
            if(this.isInterrupted())
            {
                System.out.println("Thread is interrupted ! ");
                return;
            }
            System.out.println(System.currentTimeMillis() + "");
        }
    }
}
