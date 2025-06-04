package com.example.study.arithmetic;

import java.util.*;

public class DynamicProgramming {

    public static void main(String[] args) {


//        List<Integer> test = Arrays.asList(1, 5, 11, 9);
//        System.out.println(canPartition(13, test, test.size() - 1));

//        System.out.println(mostValue(Arrays.asList(2, 1, 3), Arrays.asList(4, 2, 3), 4, 2));
//        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
//        System.out.println(LIS(nums));

//        System.out.println(countDiffWight(new int[]{2, 1}, new int[]{2, 1}, 2));
//        System.out.println(getPrimeCompanionMaxCount(new int[]{2,5,6,13}));
        System.out.println(countSoda(6));
    }


    // 凑零钱
    public static int getWays(int amount, List<Integer> coins, int n) {
        if (amount == 0) {
            return 1;
        } else if (amount < 0 || n < 0) {
            return 0;
        }
        return getWays(amount, coins, n - 1) + getWays(amount - coins.get(n), coins, n);
    }

    // 可分割相同sum子集
    public static boolean canPartition(int amount, List<Integer> coins, int n) {
        if (amount == 0) {
            return true;
        } else if (amount < 0 || n < 0) {
            return false;
        }
        return canPartition(amount, coins, n - 1) || canPartition(amount - coins.get(n), coins, n - 1);
    }

    // 背包
    public static int mostValue(List<Integer> w, List<Integer> v, int limit, int n) {
        if (limit <= 0 || n < 0) {
            return 0;
        }

        int notChoise = mostValue(w, v, limit, n - 1);
        int choise = limit - w.get(n) >= 0 ? mostValue(w, v, limit - w.get(n), n - 1) + v.get(n) : 0;
        return Math.max(notChoise, choise);
    }

    // 称砝码
    public static int countDiffWight(int[] w, int[] c, int n) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            HashSet<Integer> add = new HashSet<>();
            int ci = c[i];
            int wi = w[i];
            for (int j = 0; j < ci; j++) {
                int aaa = (j + 1) * wi;
                add.add(aaa);
                for (Integer wight : set) {
                    add.add(aaa + wight);
                }
            }
            set.addAll(add);
        }
        return set.size();
    }

    // 最长递增子序列
    public static int LIS(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);
        int res = 1;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[j] + 1, dp[i]);
                }
            }
            res = Math.max(dp[i], res);
        }
        return res;
    }

    public List<Integer> LIS(int[] nums, int n) {
        if (n==1){
            List<Integer> baseRes = new ArrayList<>();
            baseRes.add(nums[0]);
            return baseRes;
        }
        List<Integer> lis = LIS(nums, n - 1);
        if(lis.get(lis.size()-1)<nums[n-1]){
            lis.add(nums[n-1]);
        }
        return lis;

    }
    // 最长递增序列
    public static int LIS1(int[] nums) {
        int res = 0, left = 0, right = 0;
        for (; right < nums.length; right++) {
            if (right > 0 && nums[right] <= nums[right - 1]) {
                left = right;
            }
            res = Math.max(res, right - left + 1);
        }
        return res;
    }


    // 素数伴侣
    public static int countPrimeCompanion(int[] nums) {
        List<Integer> even = new ArrayList<>();
        List<Integer> odd = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int number = nums[i];
            if (number % 2 == 0) {
                even.add(number);
            } else {
                odd.add(number);
            }
        }
        int oddSize = even.size();
        int evenSize = odd.size();
        boolean[][] matchArr = new boolean[evenSize][oddSize];
        for (int i = 0; i < evenSize; i++) {
            for (int j = 0; j < oddSize; j++) {
                if (isPrimeNumber(even.get(i) + odd.get(j))) {
                    matchArr[i][j] = true;
                } else {
                    matchArr[i][j] = false;
                }
            }
        }
        return hungarian(matchArr);
    }

    public static boolean isPrimeNumber(int number) {
        double sqrt = Math.sqrt(number);
        for (int i = 2; i <= sqrt; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static int hungarian(boolean[][] map) {
        int sum = 0;
        int evenSize = map.length;
        int oddSize = map[0].length;
        //记录下已经匹配的行号，用于回溯
        Integer[] columnMatch = new Integer[oddSize];
        for (int i = 0; i < evenSize; i++) {
            //记录下这列是否被访问过
            boolean[] visit = new boolean[oddSize];
            if (match(i, map, columnMatch, visit)) {
                sum++;
            }
        }
        return sum;
    }

    private static boolean match(int i, boolean[][] map, Integer[] columnMatch, boolean[] visit) {
        for (int j = 0; j < map[0].length; j++) {
            if (map[i][j] && !visit[j]) {
                visit[j] = true;
                //如果这列没有匹配的行则将该列的行号记录为当前行，
                //如果有则回溯已被占用行，如果已被占用行存在下一个匹配值则替换
                if (columnMatch[j] == null || match(columnMatch[j], map, columnMatch, visit)) {
                    columnMatch[j] = i;
                    return true;
                }
            }
        }
        return false;
    }

    // 空瓶子换汽水
    public static int countSoda(int num) {
        if (num == 0 || num == 1) {
            return 0;
        } else if (num == 2) {
            return 1;
        }
        return 1 + countSoda(num - 2);
    }


}
