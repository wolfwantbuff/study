package com.example.study.juc;

import java.util.concurrent.Semaphore;

/**
 * @author xiaodong
 * @date 2022/11/23 16:41
 * @wiki
 */
public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(
                    () -> {
                        try {
                            semaphore.acquire();
                            System.out.println("第"+ finalI +"个人获得许可");
                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        } finally {
                            semaphore.release();
                        }
                    }
            ).start();
        }
        Thread.sleep(2000);
        new Thread(
                () -> {
                    if(semaphore.tryAcquire()){
                        System.out.println("第"+ 4 +"个人获得许可");
                        semaphore.release();
                    }else {
                        System.out.println("第"+ 4 +"个人未获得许可");
                    }
                }
        ).start();
//        Thread.sleep(2000);
//        new Thread(
//                () -> {
//                    try {
//                        semaphore.acquire();
//                    } catch (InterruptedException interruptedException) {
//                        interruptedException.printStackTrace();
//                    } finally {
//                        semaphore.release();
//                    }
//                    System.out.println("第"+ 4 +"个人获得许可");
//                }
//        ).start();
    }
}
