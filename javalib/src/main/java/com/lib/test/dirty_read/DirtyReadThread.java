package com.lib.test.dirty_read;

/**
 * Created by JerryCaicos on 2017/9/7.
 */

public class DirtyReadThread extends Thread
{
    private DirtyReadObject dirtyReadObject;
    public DirtyReadThread(DirtyReadObject object)
    {
        super();
        dirtyReadObject = object;
    }

    @Override
    public void run()
    {
        super.run();
        dirtyReadObject.setValue("B","BBBB");
    }
}
