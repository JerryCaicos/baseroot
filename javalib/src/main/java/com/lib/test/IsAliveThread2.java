package com.lib.test;

/**
 * Created by chenaxing on 2017/8/17.
 */

public class IsAliveThread2 extends Thread
{
    public IsAliveThread2()
    {
        System.out.println("IsAliveThread2 begin ");
        System.out.println("Thread.currentThread.getName() = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread.isAlice() = " + Thread.currentThread().isAlive());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("this.isAlive() = " + this.isAlive());
        System.out.println("IsAliveThread2 end");
    }

    @Override
    public void run()
    {
        super.run();
        System.out.println("IsAliveThread2 run begin");
        System.out.println("Thread.currentThread.getName() = " + Thread.currentThread().getName());
        System.out.println("Thread.currentThread.isAlive() = " + Thread.currentThread().isAlive());
        System.out.println("this.getName() = " + this.getName());
        System.out.println("this.isAlive() = " + this.isAlive());
        System.out.println("IsAliveThread2 run end");
    }
}
