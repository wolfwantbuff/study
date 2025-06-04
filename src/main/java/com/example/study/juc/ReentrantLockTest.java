package com.example.study.juc;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author xiaodong
 * @date 2022/11/15 15:36
 * @wiki
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        new Thread(
                () -> {
                    try {
                        lock.lock();
//                        lock.lockInterruptibly();
                        System.out.println("222");
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                }
        ).start();
        try {
            Thread.sleep(1000);
            lock.lock();
            System.out.println("333");
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
}
