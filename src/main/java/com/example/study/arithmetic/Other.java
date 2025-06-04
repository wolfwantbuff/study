package com.example.study.arithmetic;

public class Other {
    public static void main(String[] args) {
        System.out.println(getLeastCommonMmultiple(4, 10));
    }

    public static int getLeastCommonMmultiple(int a, int b) {
        int start = Math.max(a, b);
        int end = a * b;
        while (start <= end) {
            if (start % a == 0 && start % b == 0) {
                break;
            }
            start++;
        }
        return start;
    }
}
