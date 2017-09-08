package com.lib.test.synchronized_lock_reload;

/**
 * Created by JerryCaicos on 2017/9/8.
 */

public class SubLockObject extends LockObject
{
    synchronized public void operateIndexInSub()
    {
        while(index > 0)
        {
            try
            {
                index--;
                System.out.println("SubLockObject class print, index = " + index);
                Thread.sleep(1000);
                this.operateIndex();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
