package com.lib.test.synchronized_lock_reload;

/**
 * Created by JerryCaicos on 2017/9/8.
 */

public class TestService
{
    synchronized public void doServiceOne()
    {
        System.out.println("function do service one");
        doServiceTwo();
    }

    synchronized public void doServiceTwo()
    {
        System.out.println("function do service two");
        doServiceThree();
    }

    synchronized public void doServiceThree()
    {
        System.out.println("function to service three");
    }
}
