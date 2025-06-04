package com.example.study.systemdesign;

import java.util.Random;

/**
 * @author xiaodong
 * @date 2023/8/1 14:48
 * @wiki
 */
public class RedPackage {


    public static void main(String[] args) {
        Integer[] split = split(100, 10);
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }

    public static Integer[] split(int total, int count) {
        int use = 0;
        Integer[] res = new Integer[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            if (i == count - 1) {
                res[i] = total - use;
            } else {
                int avg = (total - use) / (count - i) * 2;
                int i1 = random.nextInt(avg - 1);
                res[i] = 1 + i1;
            }
            use += res[i];
        }
        return res;
    }
}
