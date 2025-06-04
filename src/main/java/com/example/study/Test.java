package com.example.study;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) {
        System.out.println(getmax("abca"));
    }

    public static String getmax(String s) {
        int start = 0, end = 0;
        int left = 0;
        int right = 0;
        HashMap<Character, String> map = new HashMap<>();
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;
            if (map.get(c) == null) {
                map.put(c, "");
            } else {
                if (right - left > end - start) {
                    start = left;
                    end = right-1;
                }
                left=right;
                map.clear();
            }

        }
        return s.substring(start,end);
    }

}
