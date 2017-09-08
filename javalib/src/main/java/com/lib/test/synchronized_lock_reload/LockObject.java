package com.lib.test.synchronized_lock_reload;

/**
 * Created by JerryCaicos on 2017/9/8.
 */

public class LockObject
{
    public int index = 10;
    synchronized public void operateIndex()
    {
        try
        {
            index--;
            System.out.println("LockObject class print, index = " + index);
            Thread.sleep(110);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
