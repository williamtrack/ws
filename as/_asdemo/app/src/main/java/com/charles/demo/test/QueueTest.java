package com.charles.demo.test;

import android.util.Log;

import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueueTest {
    /* Queue Deque
      add        增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
      remove   移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
      element  返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
      offer       添加一个元素并返回true       如果队列已满，则返回false
      poll         移除并返问队列头部的元素    如果队列为空，则返回null
      peek       返回队列头部的元素             如果队列为空，则返回null
      put         添加一个元素                      如果队列满，则阻塞
      take        移除并返回队列头部的元素     如果队列为空，则阻塞
     */

    static Deque<Long> deque = new LinkedList<>();
    static Queue<Long> queue = new PriorityQueue<>();

    public static void job1() {
        deque.offer(12L);
        deque.offerFirst(13L); //队首插入元素
        Log.d("William", "QueueTest-job1: " + deque.poll());
        Log.d("William", "QueueTest-job1: " + deque.poll());
        Log.d("William", "QueueTest-job1: " + deque.poll());

        queue.offer(12L);
        queue.offer(13L);
        Log.d("William", "QueueTest-job1: " +queue.poll());
        Log.d("William", "QueueTest-job1: " +queue.poll());
        Log.d("William", "QueueTest-job1: " +queue.poll());

    }
}
