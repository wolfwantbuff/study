package com.example.study.juc;

import sun.misc.Unsafe;

public class UnsafeTest {
    static final Unsafe unsafe= Unsafe.getUnsafe();

    static final long stateOffset ;
    private volatile long state=0;
    static {
        try {
            stateOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("state"));
        }catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            throw new Error(ex);
        }
    }

    public static void main(String[] args) {
        UnsafeTest unsafeTest = new UnsafeTest();
        boolean b = unsafe.compareAndSwapInt(unsafeTest, stateOffset, 0, 1);
        System.out.println(b);
    }
}
