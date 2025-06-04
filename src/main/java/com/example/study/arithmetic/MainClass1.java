package com.example.study.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainClass1 {

    public static void main(String[] args) {
        List<Integer> result = getStateServerCount(6,
                Arrays.asList(Arrays.asList(3,2), Arrays.asList(4,3), Arrays.asList(2,6), Arrays.asList(6,3)),
                Arrays.asList(1,2,3,4,5,6),
                1
        );
        for (int i : result) {
            System.out.println(i);
        }
    }


    public static List<Integer> getStateServerCount(int n, List<List<Integer>> log_data, List<Integer> query, Integer X) {
        List<Integer> result = new ArrayList<>();
        for (int i : query) {
            Set<Integer> ds = new HashSet<>();
            for (List<Integer> log : log_data) {
                if (log.get(1) <= i && log.get(1) >= i - X) {
                    ds.add(log.get(0));
                }
            }
            result.add(n - ds.size());
        }
        return result;
    }

}
