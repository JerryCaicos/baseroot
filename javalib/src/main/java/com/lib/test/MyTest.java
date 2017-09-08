package com.lib.test;

import com.lib.test.chapter_one.CountThread;
import com.lib.test.chapter_one.CurrentThread;
import com.lib.test.chapter_one.DaemonThread;
import com.lib.test.chapter_one.InterrputThread;
import com.lib.test.chapter_one.IsAliveThread;
import com.lib.test.chapter_one.IsAliveThread2;
import com.lib.test.chapter_one.LoginThread;
import com.lib.test.chapter_one.MyThread;
import com.lib.test.chapter_one.ReturnInterruptThread;
import com.lib.test.chapter_one.SleepThread1;
import com.lib.test.chapter_one.SleepThread2;
import com.lib.test.chapter_one.YieldThread;
import com.lib.test.dirty_read.DirtyReadObject;
import com.lib.test.dirty_read.DirtyReadThread;

import java.util.Calendar;
import java.util.TimeZone;

public class MyTest
{
    public static void main(String[] strings)
    {
//        testStartAndRunFunc();
//        testSynchronized();
//        testCountDesc();
//        testCurrentThread();
//        testThreadIsAlive();
//        testThreadIsAlive2();
//        testSleepThread();
//        System.out.println("time = " + getTimesMorning());
//        System.out.println("time = " + System.currentTimeMillis());
//        testInterruptThread();

//        testInterruptState();
//        testReturnInterruptThread();
//        testYieldFunc();
//        testDaemonThread();
//        testString();
        testDirtyRead();
    }

    /**
     * <p>
     *     synchronized 关键字拥有锁重入的功能，也就是在使用synchronized 时，当一个线程得到一个对象锁后，再次请求
     *     此对象锁时是可以再次得到该对象的锁的。这也证明，synchronized 方法/代码块 的内部调用本类的其他的 synchronized
     *     方法/代码块 时，是永远可以得到锁的。
     * </p>**/
    private static void synchronizedLockReload()
    {

    }

    /**
     * <p>
     *     synchronized 同步关键字
     *     1、两个线程访问同一个对象实例中的同步方法是线程安全的，另外，方法内的变量是线程安全的。
     *     2、synchronized 关键字取得的锁都是对象锁，而不是一段代码或者一个方法的锁。那个线程先执行synchronized的方法，
     *     那个线程就持有该方法所属对象的锁，那么其他线程就只能呈现等待状态，前提是多个线程访问的是同一个对象。
     *     3、如果多个线程访问多个对象，则JVM会创建多个锁。
     *     4、调用synchronized声明的方法一定是排队执行的。另外，要牢记住“共享”，只有共享资源的读写访问才需要同步处理，
     *     非共享资源根本没有同步的必要。
     *     5、A 线程先持有 object对象的Lock锁，B 线程可以异步的调用object对象中非synchronized类型的方法（会出现脏读）。
     *     6、A 线程先持有 object对象的Lock锁，B 线程如果在此时想要调用object对象的synchronized类型的方法，则需要
     *     排队等待，也就是同步。
     * </p>
     * <p>
     *     对比getValue()方法没有声明synchronized和声明了synchronized 的结果。
     *     在getValue()没有被synchronized关键字修饰时，出现了数据脏读。
     *     1、当A 线程调用object对象的加入了synchronized关键字的 X（方法名） 方法时，A 线程就获得了 X 方法锁，确切的讲，
     *     是获得了object对象的锁，所以其他线程必须等A 线程执行完才可以调用 X 方法，但是B 线程可以随意调用其他的非synchronized
     *     同步的 Y 方法，如果X 和Y 方法中有引用相同的变量，这个时候就会出现脏读。
     *     2、当A 线程调用object对象的加入了synchronized关键字的 X（方法名） 方法时，A 线程就获得了 X 方法锁，确切的讲，
     *     是获得了object对象的锁，所以其他线程必须等A 线程执行完才可以调用 X 方法，而B 线程如果调用声明了synchronized
     *     关键字的 Y 方法时，B 线程必须等 A 线程执行完X 方法，才可以调用到 Y 方法，也就是 A 线程释放了object的对象锁
     *     以后，B 线程才可以调用object 声明了synchronized关键字的 Y 方法。这样可以解决数据脏读的情况。
     * </p>**/

    private static void testDirtyRead()
    {
        try
        {
            DirtyReadObject object = new DirtyReadObject();
            DirtyReadThread thread = new DirtyReadThread(object);
            thread.start();
            Thread.sleep(2000);
            object.getValue();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static void testString()
    {
        StringBuffer param = new StringBuffer();
        param.append(4334);
        param.append("_");
        param.append("sfdsfsf");
        System.out.println(param.toString());
    }

    /**
    * <p>
     *     Java中有两种线程，用户线程 和 守护线程。
     *     守护线程是一种特殊的线程，它的特性有“陪伴”的含义，当进程中不存在非守护线程了，守护线程会自动销毁。典型的守护线程
     *     就是 垃圾回收线程。当进程中没有非守护线程了，则垃圾回收线程就没有存在的必要了，自动销毁。
    * </p>**/
    private static void testDaemonThread()
    {
        try
        {
            DaemonThread thread = new DaemonThread();
            thread.setDaemon(true);
            thread.start();
            Thread.sleep(5000);
            System.out.println("if I leave, thread will not print any thing, it's stop");
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *     线程优先级：在操作系统中，线程可以划分优先级。优先级高的线程得到的CPU资源多，也就是CPU优先执行优先级较高的
     *     线程对象中的任务。设置线程的优先级可以使用 setPriority() 方法。在Java中，线程的优先级分为 1~10 。如果
     *     优先级 < 1 或者 > 10 ,jdk会抛出异常：throw new IllegalArgumentException()。</>
     * </p>
     * <p>
     *     线程优先级具有继承性。比如 A 线程启动了 B 线程，那么 B 线程的优先级和 A　线程的优先级是相同的。
     *     高优先级的线程总是大部分先执行完，但不代表高优先级的线程全部先执行完毕。当线程的优先级相差很大时，谁先执行完
     *     和代码的调用顺序无关。这说明线程的优先级具有规则性，也就是CPU尽量将资源让给优先级高的线程。
     * </p>
     *
     * <p>
     *     前面说，线程的优先级高则优先执行完run方法中的任务，但是这个结果不能说的太肯定。因为线程的优先级还具有随机性。
     *     也就是优先级高的线程不一定每次都先执行完。不要把线程的优先级和运行结果的顺序作为衡量的标准，优先级高是线程不一定
     *     每次都先执行完run方法中的任务。
     * </p>**/

    /**
     * <p>
     *     yield() 方法的作用是放弃当前的CPU资源，让给其他的任务去占用CPU执行。
     *     但是放弃的时间不确定，有可能刚刚放弃，马上又重新获得了CPU。
     * </p>**/
    public static void testYieldFunc()
    {
        YieldThread thread = new YieldThread();
        thread.start();
    }

    /**<p>
     * 将方法 interrupt() 与 return 结合使用，也能实现停止线程的效果。
     * <p/>**/
    private static void testReturnInterruptThread()
    {
        try
        {
            ReturnInterruptThread thread = new ReturnInterruptThread();
            thread.start();
            Thread.sleep(2000);
            thread.interrupt();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**如果判断一个线程的状态是不是停止的，在Java的jdk中提供了两种方法：1、this.interrupted()，测试当前线程是否已经中断，
     * 当前线程是指运行 this.interrupted() 的线程。
     * 2、this.isInterrupted()，测试线程是否已经中断。**/
    /** 停止一个线程：在sleep() 时停止线程，先sleep再interrupt，会进入catch语句，并清除停止状态，使之变成false。
     * 也要注意先interrupt再sleep**/
    private static void testInterruptState()
    {
        try
        {
            InterrputThread thread = new InterrputThread();
            thread.start();
            Thread.sleep(2000);
            /**调用 thread.interrupt() ，查看interrupted()方法的内部实现，发现它是static的，return currentThread().isInterrupted(true);
             * 返回的是 currentThread()是否已经中断，而currentThread()是main，所以main线程一直未停止过。**/
            thread.interrupt();
            /**调用Thread.currentThread().interrupt();时，注意两个Boolean值，第一个为true，第二个为false，
             * 出现这种情况是因为 interrupted() 测试当前线程是否已经中断，该方法会将线程的中断状态清除。换句话说，就是
             * 如果连续两次调用该方法，则第二次将返回false，因为第一次清除了状态（在第一次清除了其中断的状态之后，且第二次
             * 调用校验完中断状态前，当前线程再次中断的情况除外。）。
             * 而在调用isInterrupted()方法测试状态时，不会清除当前的中断状态。**/
//            Thread.currentThread().interrupt();
            System.out.println("thread is interrupted 1 : " + thread.interrupted());
            System.out.println("thread is interrupted 2 : " + thread.interrupted());
            /****/
//            System.out.println("thread is interrupted 3 : " + thread.isInterrupted());
//            System.out.println("thread is interrupted 4 : " + thread.isInterrupted());

            /**总结：
             * 1、this.interrupted() ： 判断当前线程是否是中断状态，执行后具有将状态标识清除的功能。
             * 2、this.inInterrupted() ：测试线程Thread对象是否是中断状态，但不清除状态标识。**/
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**interrupt() 方法的使用效果并不像 for+break 语句那样，马上就停止循环，调用 interrupt() 方法仅仅是在当前
     * 线程中打了一个停止的标记，并不是真正的停止线程。**/
    private static void testInterruptThread()
    {
        try
        {
            InterrputThread thread = new InterrputThread();
            thread.start();
            Thread.sleep(10);
            thread.interrupt();
            /**从示例结果可以看出，虽然调用了interrput()方法，但是线程并没有中止，而是一直执行结束。**/
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**<p>
     * 停止一个线程意味着在线程处理完任务之前停掉正在做的操作，也就是放弃当前的操作。虽然看起来简单，但是必须做好
     * 防范措施，以便达到预期的效果。停止一个线程可以使用Thread.stop()方法，但是这个方法已经废弃，后期可能会不支持
     * 或者不可用，而且它是不安全的。
     * <p/>
     * <p>
     * 大多数停止一个线程的操作是使用Thread.interrupt()方法，尽管方法名称是“停止、中止” 的意思，但这个方法
     * 不会停止一个正在运行的线程，还需要加入一个判断才能完成线程的中止。
     * <p/>
     * <p>
     * 在Java中有以下3种方法可以停止一个线程：
     * 1、使用退出标志，使线程正常退出，也就是run()方法完成后停止。
     * 2、使用stop()方法进行中止，但是不推荐使用这个方法，因为stop(),suspend(),resume()方法都是过期作废的
     * 3、使用interrupt()方法中断线程。
     * </p>
     * <p>
     *    调用stop() 方法会抛出java.lang.ThreadDeath异常，但在通常情况下，不需要显示的捕捉该异常。方法stop()
     *    已经作废，因为如果强制让线程停止，则有可能使一些请理性的工作得不到完成。另外一种情况就是对锁定的对象进行了
     *    “解锁”，导致数据得不到同步的处理，出现数据不一致的问题。
     * </p>**/
    private static void testStopThread()
    {

    }

    private static long getTimesMorning(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**sleep() 方法的作用是在指定的毫秒时间内让当前的 “正在执行的线程” 休眠（暂停执行），
     * 这个正在执行的线程是指 this.currentThread() 返回的线程，注意run() 和 start()
     * 方法时，this.currentThread() 返回的线程是不同的**/
    private static void testSleepThread()
    {
        SleepThread1 thread1 = new SleepThread1();
        long start = System.currentTimeMillis();
        System.out.println("begin time = " + start);
        thread1.run();
        long end  = System.currentTimeMillis();
        System.out.println("end time = " + end);
        System.out.println("end - start = " + (end - start));

        System.out.println("-------------------------------------");

        SleepThread2 thread2 = new SleepThread2();
        long start2 = System.currentTimeMillis();
        System.out.println("begin2 time = " + start2);
        thread2.start();
        long end2  = System.currentTimeMillis();
        System.out.println("end2 time = " + end2);
        System.out.println("end2 - start2 = " + (end2 - start2));
    }


    /**在使用isAlive()方法时，如果将当前线程对象以构造参数的方式传递给Thread对象进行start()，
     * 运行的结果和testIsAlive()中的方式的运行结果是有差异的，造成这种差异的原因来自于
     * Thread.currentThread() 和 this 的差异。Thread.currentThread()表示当前正在运行的
     * 线程，而this指的是当前类的对象实例，new Thread(thread2)这种方式是新的一个线程**/
    private static void testThreadIsAlive2()
    {
        IsAliveThread2 thread2 = new IsAliveThread2();
        Thread t1 = new Thread(thread2);
        System.out.println("main begin t1 isAlive() = " + t1.isAlive());
        t1.setName("A");
        t1.start();
        System.out.println("main end t1 isAlive() = " + t1.isAlive());
    }

    private static void testThreadIsAlive()
    {
        try
        {
            IsAliveThread thread = new IsAliveThread();
            System.out.println("current thread status begin = " + thread.isAlive());
            /**当调用start()方法时，thread子线程有状态，当调用run()方法时，thread子线程没有状态，全部为false
             * 即 run()方法全部在调用线程中执行**/
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
        /**start() 方法是交给子线程执行，run()方法是在哪个线程调用就在哪个线程执行，注意两者的运行结果不同。**/
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
     * run是同步执行，在哪个线程调用的run()，就在哪个线程执行，一直执行完以后，才会执行后面的语句。
     * start是异步执行，是通知线程规划器当前线程已准备好，让系统安排一个时间来运行线程。
     * 执行start方法的顺序不代表线程启动的顺序
     **/
    private static void testStartAndRunFunc()
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
