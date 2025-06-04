package com.example.study.juc;

import java.util.concurrent.locks.LockSupport;

/**
 * @author xiaodong
 * @date 2022/11/14 16:30
 * @wiki
 */
public class ParkUnparkTest {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            LockSupport.park();
            System.out.println(sum);
        });
        thread.start();
        //睡眠一秒钟，保证线程A已经计算完成，阻塞在wait方法
        Thread.sleep(1000);
        LockSupport.unpark(thread);
    }
}
