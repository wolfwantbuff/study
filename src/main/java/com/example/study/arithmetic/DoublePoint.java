package com.example.study.arithmetic;

import java.util.Arrays;
import java.util.HashMap;

public class DoublePoint {
    public static void main(String[] args) {
//        System.out.println(longestPalindrome("bababd"));
//        System.out.println(getSqrt(9, 0.0001));
        System.out.println(getMinSub("debanc", "abc"));
//        int[] distinct = distinct(new int[]{2, 2, 1});

    }

    // 最长回文子串
    public static String longestPalindrome(String s) {
        char[] chars = s.toCharArray();
        String max = "";
        for (int i = 0; i < chars.length - 1; i++) {
            String palindrome = palindrome(s, chars, i, i);
            String palindrome1 = palindrome(s, chars, i, i + 1);
            String temp;
            if (palindrome.length() > palindrome1.length()) {
                temp = palindrome;
            } else {
                temp = palindrome1;
            }
            if (temp.length() > max.length()) {
                max = temp;
            }
        }
        return max;
    }

    public static String palindrome(String s, char[] chars, int left, int right) {
        while (left >= 0 && right < chars.length && chars[left] == chars[right]) {
            left--;
            right++;
        }
        return s.substring(left + 1, right);
    }

    // 最小覆盖子串
    public static String getMinSub(String s, String t) {
        char[] sCharArray = s.toCharArray();
        char[] tCharArray = t.toCharArray();
        HashMap<Character, Integer> tCharMap = new HashMap<>();
        for (char c : tCharArray) {
            tCharMap.compute(c, (k, v) -> v == null ? 1 : v + 1);
        }
        HashMap<Character, Integer> map = new HashMap<>();
        int left = 0;
        int right = 0;

        int start = 0;
        int end = Integer.MAX_VALUE;
        while (right < sCharArray.length) {
            char addChar = sCharArray[right];
            if (tCharMap.getOrDefault(addChar, 0) > 0) {
                map.compute(addChar, (k, v) -> v == null ? 1 : v + 1);
            }
            while (map.size() == tCharMap.size()) {
                char removeChar = sCharArray[left];
                if (tCharMap.getOrDefault(removeChar, 0) > 0) {
                    Integer orDefault = map.getOrDefault(addChar, 0);
                    if (orDefault == 1) {
                        map.remove(removeChar);
                        if (right - left < end - start) {
                            start = left;
                            end = right;
                        }
                    } else {
                        map.put(addChar, orDefault - 1);
                    }
                }
                left++;
            }
            right++;
        }
        return s.substring(start, end + 1);
    }

    // 开方
    public static double getSqrt(double v, double precision) {
        double left = 0;
        double right = v;
        while (right - left > precision) {
            double mid = (left + right) / 2;
            if (mid * mid > v) {
                right = mid;
            } else {
                left = mid;
            }
        }
        return (left + right) / 2;
    }

    public static int[] distinct(int[] nums) {
        Arrays.sort(nums);
        int left = 0;
        int right = 0;
        while (right < nums.length) {
            if (nums[left] == nums[right]) {
                right++;
            } else {
                left++;
                nums[left] = nums[right];
            }
        }
        int[] res = new int[left + 1];
        for (int i = 0; i < res.length; i++) {
            res[i] = nums[i];
        }
        return res;
    }


}
