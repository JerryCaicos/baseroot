package com.lib.test;

/**
 * Created by JerryCaicos on 2017/8/17.
 */

public class LoginServlet
{
    private static String userName;
    private static String password;

    /**synchronized可以在任意对象或者方法上加锁，而加锁的这段代码称为互斥区或者临界区。
     * 当一个线程想要执行同步方法里面的代码时，线程首先尝试去拿这把锁，如果能够拿到这把锁，
     * 那么这个线程就可以执行synchronized中的代码。如果拿不到这把锁，那么这个线程就会不断的
     * 尝试拿这把锁，直到拿到这把锁，而且是有多个线程同时去竞争这把锁。**/
    public synchronized static void doPost(String name,String pwd)
    {
        try
        {
            userName = name;
            if(userName.equals("a"))
            {
                Thread.sleep(2000);
            }
            password = pwd;
            System.out.println("userName = " + userName + ", password = " + password);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
