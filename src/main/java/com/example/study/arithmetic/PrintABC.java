package com.example.study.arithmetic;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class  PrintABC {
    // 第一种
    private static Object obj = new Object();
    private static int count = 0;

//    public static void main(String[] args) {
//        Printer a = new Printer(0, "a");
//        Printer b = new Printer(1, "b");
//        Printer c = new Printer(2, "c");
//        new Thread(a).start();
//        new Thread(b).start();
//        new Thread(c).start();
//    }

    static class Printer implements Runnable {
        private int state;
        private String value;

        public Printer(int state, String value) {
            this.state = state;
            this.value = value;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (obj) {
                    while (count % 3 != state) {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(value);
                    count++;
                    obj.notifyAll();
                }
            }
        }
    }

    // 第二种
    private static Object objA = new Object();
    private static Object objB = new Object();
    private static Object objC = new Object();

//    public static void main(String[] args) {
//        try {
//            new Thread(PrintABC::printA).start();
//            Thread.sleep(3000);
//            new Thread(PrintABC::printB).start();
//            Thread.sleep(3000);
//            new Thread(PrintABC::printC).start();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }


    public static void printA() {
        for (int i = 0; i < 10; i++) {
            synchronized (objC) {
                synchronized (objA) {
                    System.out.println("a");
                    objA.notify();
                }
                try {
                    objC.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printB() {
        for (int i = 0; i < 10; i++) {
            synchronized (objA) {
                synchronized (objB) {
                    System.out.println("b");
                    objB.notify();
                }
                try {
                    objA.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printC() {
        for (int i = 0; i < 10; i++) {
            synchronized (objB) {
                synchronized (objC) {
                    System.out.println("c");
                    objC.notify();
                }
                try {
                    objB.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // 第三种
    static ReentrantLock lock = new ReentrantLock();
    static Condition conditionA = lock.newCondition();
    static Condition conditionB = lock.newCondition();
    static Condition conditionC = lock.newCondition();

//

    public static void pA() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            while (count % 3 != 0) {
                try {
                    conditionA.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println("a");
            conditionB.signal();
            lock.unlock();
        }
    }

    public static void pB() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            while (count % 3 != 1) {
                try {
                    conditionB.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println("b");
            conditionC.signal();
            lock.unlock();
        }
    }

    public static void pC() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            while (count % 3 != 2) {
                try {
                    conditionC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println("c");
            conditionA.signal();
            lock.unlock();
        }
    }
}
