//package com.example.study.systemdesign;
//
//import java.math.BigInteger;
//import java.security.SecureRandom;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class HashPsiBasedOnDH {
//    public static void main(String[] args) {
//        // 随机生成两个私钥
//        BigInteger priA = BigInteger.probablePrime(1024, new SecureRandom());
//        BigInteger priB = BigInteger.probablePrime(1024, new SecureRandom());
//
//        // 生成一个大素数
//        BigInteger q = BigInteger.probablePrime(1024, new SecureRandom());
//
//        // 计算原根
//        BigInteger a = calPrimitiveRoot(q);
//
//        // 计算公钥
//        BigInteger pubA = a.modPow(priA, q);
//        BigInteger pubB = a.modPow(priB, q);
//
//        // A B的集合信息
//        Set<Integer> setA = new HashSet<>(Arrays.asList(2, 3, 4));
//        Set<Integer> setB = new HashSet<>(Arrays.asList(1, 2, 3));
//
//        // 一轮加密
//        Set<Integer> oneRoundA = setA.stream().map(e -> e.powMod(e, p)).collect(Collectors.toSet());
//        Set<Integer> oneRoundB = setB.stream().map(e -> e.powMod(e, p)).collect(Collectors.toSet());
//
//        // 二轮加密
//        Set<Integer> twoRoundA = oneRoundA.stream().map(e -> e.powMod(e, p)).collect(Collectors.toSet());
//        Set<Integer> twoRoundB = oneRoundB.stream().map(e -> e.powMod(e, p)).collect(Collectors.toSet());
//
//        // 比较重复元素
//        for (Integer d : twoRoundA) {
//            if (twoRoundB.contains(d))
//                System.out.println(d);
//        }
//    }
//
//
//    /**
//     * 计算原根静态方法
//     *
//     * @param n 目标数据 通常是m
//     * @return 原根
//     */
//    public static int calPrimitiveRoot(int n) {
//        int res = n, a = n;
//        for (int i = 2; i * i < a; i++) {
//            if (a % i == 0) {
//                res = res / i * (i - 1);
//                while (a % i == 0)
//                    a /= i;
//            }
//        }
//        if (a > 1)
//            res = res / a * (a - 1);
//        return res;
//    }
//
//}