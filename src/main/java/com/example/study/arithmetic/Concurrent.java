package com.example.study.arithmetic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent {

    static ReentrantLock lock = new ReentrantLock();
    static Condition conditionA = lock.newCondition();
    static Condition conditionB = lock.newCondition();
    static Condition conditionC = lock.newCondition();
    static int counter = 0;

    public static void main(String[] args) {
        printABC();
    }


    public static void printABC(){
        Runnable runnableA=()->print(0, "A", conditionA, conditionB);
        Runnable runnableB=()->print(1, "B", conditionB, conditionC);
        Runnable runnableC=()->print(2, "C", conditionC, conditionA);
        new Thread(runnableA).start();
        new Thread(runnableB).start();
        new Thread(runnableC).start();
    }

    public static void print(int orderNumber, String printStr, Condition condition, Condition nextCondition){
        lock.lock();
        try{
            for(int i =0; i<10;i++){
                if(counter%3!=orderNumber){
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(printStr);
                counter++;
                nextCondition.signal();
            }
        }finally {
            lock.unlock();
        }
    }
}
