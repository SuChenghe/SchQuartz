package com.suchenghe.quartz.service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 *  ExecutorService：
 *  一 Executors.newSingleThreadExecutor()：
 *              new ThreadPoolExecutor(1, 1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
 *  二 Executors.newCachedThreadPool()：
 *              new ThreadPoolExecutor(0, Integer.MAX_VALUE,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
 *  三 Executors.newFixedThreadPool(10)：
 *              new ThreadPoolExecutor(10,10,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
 *
 *  ScheduledExecutorService（interface extends ExecutorService）：
 *  四 Executors.newScheduledThreadPool(5)：
 *              new ScheduledThreadPoolExecutor(5);
 *
 *  keepAliveTime：非核心线程的闲置超时时间，超过这个时间就会被回收。
 *
 * @author SuChenghe
 * @date 2018/7/12 11:28
 */
public class MyThreadExecutor {

    /**
     * 自定义线程池
     */
    public static ExecutorService myThreadExecutor = new ThreadPoolExecutor(5, 5,
            0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),new MyThreadExecutor.PictureThreadFactory());

    /**
     * 定时任务线程
     */
    public static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    /**
     * 自定义线程工厂
     */
    static class PictureThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        PictureThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "my-thread-executor-pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    /**
     * 线程池测试
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //Runnable
        myThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable接口的方法");
            }
        });

        //Callable&Future
        Future future = myThreadExecutor.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                System.out.println("Callable接口的方法开始");
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "我是第一个Callable的返回结果";
            }
        });

        System.out.println("future获取的Callable接口方法的结果:"+future.get());

        Future future3 = myThreadExecutor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("第二个Callable接口的方法开始，需要执行10秒");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //取消执行
        future3.cancel(true);
        System.out.println("第二个Callable接口的线程，取消执行");


//        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("第四个任务每两秒执行一次......");
//            }
//        }, 1,2, TimeUnit.SECONDS);

    }

}
