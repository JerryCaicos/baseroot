package com.lib.test.synchronized_lock_reload;

/**
 * Created by JerryCaicos on 2017/9/8.
 */

public class LockReloadThread extends Thread
{
    public SubLockObject subLockObject;

    public LockReloadThread(SubLockObject object)
    {
        subLockObject = object;
    }

    @Override
    public void run()
    {
        System.out.println("LockReloadThread class print begin");
        super.run();
        TestService testService = new TestService();
        testService.doServiceOne();

//        SubLockObject subLockObject = new SubLockObject();
//        subLockObject.operateIndexInSub();
        subLockObject.operateIndexInSub();
        System.out.println("LockReloadThread class print end");
    }
}
