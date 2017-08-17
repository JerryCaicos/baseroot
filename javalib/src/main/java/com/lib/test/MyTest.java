package com.lib.test;

public class MyTest
{
    public static void main(String[] strings)
    {
        try
        {
            System.out.println(Thread.currentThread().getName());
            MyThread thread = new MyThread();
            thread.setName("MyThread");
            /**run是同步执行，一直执行完以后，才会执行后面的语句。start是异步执行，是通知线程规划器
             * 当前线程已准备好，让系统安排一个时间来运行线程。执行start方法的顺序不代表线程启动的顺序**/
//            thread.run();
            thread.start();
            for(int i = 0; i < 10; i++)
            {
                int time = (int) (Math.random()*1000);
                Thread.sleep(time);
                System.out.println("main = " + Thread.currentThread().getName());
            }
            System.out.println("main run end!");
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
