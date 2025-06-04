package com.example.study.juc;

import java.util.concurrent.CountDownLatch;

/**
 * @author xiaodong
 * @date 2022/11/28 11:39
 * @wiki
 */
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        new Thread(
                () -> {
                    System.out.println("1");
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    countDownLatch.countDown();
                    System.out.println("11");
                }
        ).start();
        new Thread(
                () -> {
                    System.out.println("2");
                    countDownLatch.countDown();
                    System.out.println("22");
                }
        ).start();
        new Thread(
                () -> {
                    System.out.println("3");
                    countDownLatch.countDown();
                    System.out.println("33");
                }
        ).start();
//        Thread.sleep(5000);
//        countDownLatch.countDown();
        countDownLatch.await();
    }
}
