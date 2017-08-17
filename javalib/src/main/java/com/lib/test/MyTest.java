package com.lib.test;

public class MyTest
{
    public static void main(String[] strings)
    {
//        testStartAndRun();
//        testSynchronized();
//        testCountDesc();
//        testCurrentThread();
//        testIsAlive();
        testIsAlive2();
    }

    /**在使用isAlive()方法时，如果将当前线程对象以构造参数的方式传递给Thread对象进行start()，
     * 运行的结果和testIsAlive()中的方式的运行结果是有差异的，造成这种差异的原因来自于
     * Thread.currentThread() 和 this 的差异。Thread.currentThread()表示当前正在运行的
     * 线程，而this指的是当前类的对象实例，new Thread(thread2)这种方式是新的一个线程**/
    private static void testIsAlive2()
    {
        IsAliveThread2 thread2 = new IsAliveThread2();
        Thread t1 = new Thread(thread2);
        System.out.println("main begin t1 isAlive() = " + t1.isAlive());
        t1.setName("A");
        t1.start();
        System.out.println("main end t1 isAlive() = " + t1.isAlive());
    }

    private static void testIsAlive()
    {
        try
        {
            IsAliveThread thread = new IsAliveThread();
            System.out.println("current thread status begin = " + thread.isAlive());
            /**当调用start()方法时，thread子线程有状态，当调用run()方法时，thread子线程没有状态，全部为false
             * 即 run()方法全部在主线程中执行**/
            thread.start();
//            thread.run();
            Thread.sleep(1000);
            System.out.println("current thread status end = " + thread.isAlive());
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static void testCurrentThread()
    {
        System.out.println("current running thread name = " + Thread.currentThread().getName());
        CurrentThread thread = new CurrentThread();
        System.out.println("CurrentThread start");
        /**start() 方法是交给子线程执行，run()方法是有主线程执行，注意两者的运行结果不同。**/
        thread.start();
//        thread.run();
    }

    /**println()方法和 count-- 同时使用时，可能会出现异常情况，虽然println() 方法内部是同步的，
     * 但是 count-- 操作却是在println()之前发生的，所以有发生非线程安全的概率，为了避免非线程安全，
     * 还是应该继续使用synchronized方法**/
    private static void testCountDesc()
    {
        CountThread countThread = new CountThread();
        Thread t1 = new Thread(countThread);
        Thread t2 = new Thread(countThread);
        Thread t3 = new Thread(countThread);
        Thread t4 = new Thread(countThread);
        Thread t5 = new Thread(countThread);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

    /**
     * <p>
     * <b>synchronized</b>    可以在任意对象或者方法上加锁，而加锁的这段代码称为互斥区或者临界区。
     * 当一个线程想要执行同步方法里面的代码时，线程首先尝试去拿这把锁，如果能够拿到这把锁，
     * 那么这个线程就可以执行synchronized中的代码。如果拿不到这把锁，那么这个线程就会不断的
     * 尝试拿这把锁，直到拿到这把锁，而且是有多个线程同时去竞争这把锁。
     * </p>
     * <p>
     * <b>非线程安全</b>     是指多个线程对同一个对象中的同一个实例变量进行操作时，会出现值被更改、
     * 值不同步的情况，进而影响程序的执行流程。
     * </p>
     **/
    private static void testSynchronized()
    {
        LoginThread threadA = new LoginThread("a", "as");
        LoginThread threadB = new LoginThread("b", "bb");
        threadA.start();
        threadB.start();
    }

    /**
     * run是同步执行，一直执行完以后，才会执行后面的语句。start是异步执行，是通知线程规划器
     * 当前线程已准备好，让系统安排一个时间来运行线程。执行start方法的顺序不代表线程启动的顺序
     **/
    private static void testStartAndRun()
    {
        try
        {
            System.out.println(Thread.currentThread().getName());
            MyThread thread = new MyThread();
            thread.setName("MyThread");
            //            thread.run();
            thread.start();
            for(int i = 0; i < 10; i++)
            {
                int time = (int) (Math.random() * 1000);
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
