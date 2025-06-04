package com.example.study.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author xiaodong
 * @date 2022/11/23 9:36
 * @wiki
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> {
            System.out.println("开始游戏");
        });
        new Thread(
                () -> {
                    System.out.println("第一个玩家进入房间");
                    try {
                        cyclicBarrier.await();
//                        try {
//                            cyclicBarrier.await(0, TimeUnit.NANOSECONDS);
//                        } catch (TimeoutException e) {
//                            e.printStackTrace();
//                        }
                    } catch (InterruptedException | BrokenBarrierException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("第一个玩家进入游戏");
                }
        ).start();

        new Thread(
                () -> {
                    System.out.println("第二个玩家进入房间");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("第二个玩家进入游戏");
                }
        ).start();

        new Thread(
                () -> {
                    System.out.println("第三个玩家进入房间");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException | BrokenBarrierException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("第三个玩家进入游戏");
                }
        ).start();
    }
}
