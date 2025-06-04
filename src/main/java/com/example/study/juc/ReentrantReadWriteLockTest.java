package com.example.study.juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author xiaodong
 * @date 2022/11/22 11:42
 * @wiki
 */
public class ReentrantReadWriteLockTest {
    public static void main(String[] args) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        readLock.lock();
        System.out.println("666");
        readLock.unlock();
        writeLock.unlock();
        new Thread(
                () -> {
                    try {
                        readLock.lock();
                        System.out.println("222");
                        Thread.sleep(300000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }finally {
                        readLock.unlock();
                    }
                }
        ).start();
        new Thread(
                () -> {
                    try {
                        Thread.sleep(2000);
                        readLock.lock();
                        readLock.lock();
                        System.out.println("333");
                        Thread.sleep(20000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    } finally {
                        readLock.unlock();
                        readLock.unlock();
                    }
                }
        ).start();

        try {
            readLock.lock();
            System.out.println("444");
            Thread.sleep(300000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } finally {
            readLock.unlock();
        }

    }
}
