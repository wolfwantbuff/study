package com.example.study.juc;

/**
 * @author xiaodong
 * @date 2022/11/14 16:29
 * @wiki
 */
public class WaitNotifyTest {
    public static void main(String[] args) throws Exception {
        final Object obj = new Object();
        Thread A = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            try {
                synchronized (obj) {
                    obj.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sum);
        });
        A.start();
        //睡眠一秒钟，保证线程A已经计算完成，阻塞在wait方法
//        Thread.sleep(1000);
        synchronized (obj) {
            obj.notify();
        }
    }
}
