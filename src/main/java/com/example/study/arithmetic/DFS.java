package com.example.study.arithmetic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DFS {


    public static void main(String[] args) {
//        int[][] orannges = {
//                {0, 2, 0},
//                {0, 1, 0},
//                {1, 1, 1},
//                {1, 2, 1}
//        };
//        System.out.println(orangesRotting(orannges));


    }

    public static int closedIsland(int[][] oranges) {
        int count = 0;
        for (int i = 0; i < oranges.length; i++) {
            for (int j = 0; j < oranges[0].length; j++) {
                if (oranges[i][j] == 1) {
                    count++;
                    dfsRemoveIsland(oranges, i, j);
                }
            }
        }

        return count;
    }

    private static void dfsRemoveIsland(int[][] oranges, int i, int j) {
        if (i < 0 || j < 0 || i >= oranges.length || j >= oranges[0].length) {
            return;
        }
        if (oranges[i][j] == 0) {
            return;
        }

        oranges[i][j] = 0;
        dfsRemoveIsland(oranges, i + 1, j);
        dfsRemoveIsland(oranges, i - 1, j);
        dfsRemoveIsland(oranges, i, j + 1);
        dfsRemoveIsland(oranges, i, j - 1);
    }

    // 腐烂的橘子
    public static int orangesRotting(int[][] oranges) {
        int[][] clone = oranges.clone();
        for (int i = 0; i < oranges.length; i++) {
            clone[i] = oranges[i].clone();
        }
        for (int i = 0; i < clone.length; i++) {
            for (int j = 0; j < clone[0].length; j++) {
                int e = clone[i][j];
                if (e == 2) {
                    dfsRemoveRottingOrange(clone, i, j);
                }
            }
        }
        for (int i = 0; i < clone.length; i++) {
            for (int j = 0; j < clone[0].length; j++) {
                int e = clone[i][j];
                if (e == 1) {
                    return -1;
                }
            }
        }

        int minutes = 0;
        while (true) {
            HashSet<String> set = new HashSet<>();
            boolean change = false;
            for (int i = 0; i < oranges.length; i++) {
                for (int j = 0; j < oranges[0].length; j++) {
                    int e = oranges[i][j];
                    if (e == 2) {
                        if (!set.contains(i + "" + j)) {
                            boolean maked = makeRotting(oranges, i + 1, j, set) |
                                    makeRotting(oranges, i - 1, j, set) |
                                    makeRotting(oranges, i, j + 1, set) |
                                    makeRotting(oranges, i, j - 1, set);
                            if (maked) {
                                change = true;
                            }
                        }
                    }
                }
            }
            if (change) {
                minutes++;
            } else {
                break;
            }
        }
        return minutes;
    }

    private static void dfsRemoveRottingOrange(int[][] oranges, int i, int j) {
        if (i < 0 || j < 0 || i >= oranges.length || j >= oranges[0].length) {
            return;
        }
        if (oranges[i][j] == 0) {
            return;
        }
        oranges[i][j] = 0;
        dfsRemoveRottingOrange(oranges, i + 1, j);
        dfsRemoveRottingOrange(oranges, i - 1, j);
        dfsRemoveRottingOrange(oranges, i, j + 1);
        dfsRemoveRottingOrange(oranges, i, j - 1);
    }


    private static boolean makeRotting(int[][] oranges, int i, int j, HashSet<String> set) {
        if (i >= 0 && i < oranges.length && j >= 0 && j < oranges[0].length) {
            if(oranges[i][j] ==1){
                oranges[i][j] = 2;
                set.add(i + "" + j);
                return true;
            }
        }
        return false;
    }

}
