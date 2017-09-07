package com.lib.test.dirty_read;

/**
 * Created by JerryCaicos on 2017/9/7.
 */

public class DirtyReadObject
{
    public String userName;
    public String password;

    synchronized public void setValue(String name, String pwd)
    {
        try
        {
            this.userName = name;
            Thread.sleep(5000);
            this.password = pwd;
            System.out.println("setValue method thread name : " + Thread.currentThread().getName()
                    + ", userName : " + this.userName
                    + ", password : " + this.password);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    synchronized public void getValue()
    {
        System.out.println("getValue method thread name : " + Thread.currentThread().getName()
                + ", userName : " + this.userName
                + ", password : " + this.password);
    }

}
