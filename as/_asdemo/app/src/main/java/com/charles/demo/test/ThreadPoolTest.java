package com.charles.demo.test;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void job() {
        ThreadPoolTest.initTestThreadPool();

        try {
            runTestRunnable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //平时都用runnable，但是你需要返回值时候，那么就得用callable
    public static class testRunnable implements Callable<Integer> {
        private final int i;

        public testRunnable(int i) {
            this.i = i;
        }

        @Override
        public Integer call() throws Exception {
            Log.d("William", "执行" + i);
            Thread.sleep(2000);
            Log.d("William", "执行完成" + i);
            return i;
        }
    }


    public static List<Integer> runTestRunnable() throws Exception {
        List<Future<Integer>> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = ThreadPoolTest.post(new ThreadPoolTest.testRunnable(i));
            list.add(future);//返回值先存进去别取
        }
        //future.get()这个方法有坑，因为get这个方法是阻塞的，所以，需要以上线程调用完了，再单独取值，不然，你直接在上面for循环里get的话，线程执行就会被阻塞
        for (Future<Integer> future : list) {
            Log.d("William", "执行结果=" + future.get());//这里单独取
            list1.add(future.get());
        }
        return list1;
    }


    private static ThreadPoolTest mInstance;
    private ThreadPoolExecutor mThreadPoolExec;
    private static int MAX_POOL_SIZE;
    private static final int KEEP_ALIVE = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    //初始化一个自定义线程池
    public static synchronized void initTestThreadPool() {
        if (mInstance == null) {
            mInstance = new ThreadPoolTest();
        }
    }

    public static Future<Integer> post(Callable<Integer> runnable) {
        Future<Integer> taskFuture = mInstance.mThreadPoolExec.submit(runnable);
        return taskFuture;
    }


    private ThreadPoolTest() {
        int coreNum = Runtime.getRuntime().availableProcessors();//核心线程数
        Log.d("William", "cpu核心数=" + coreNum);
        MAX_POOL_SIZE = coreNum + 1;//线程池大小
        mThreadPoolExec = new ThreadPoolExecutor(
                coreNum,
                MAX_POOL_SIZE,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                workQueue);
    }

    public static void finish() {//中断线程池执行队列
        mInstance.mThreadPoolExec.shutdown();
    }
}
