package com.example.study.arithmetic;


import org.assertj.core.util.Lists;

import java.util.*;

public class Test {
    public static List<Integer> getStaleServerCount(int n, List<List<Integer>> log_data, List<Integer> query, int X) {
        // Write your code here

        List<Integer> result = new ArrayList<>();
        for (int queryElement : query) {
            HashSet<Integer> receieved = new HashSet<>();
            for (List<Integer> log : log_data) {
                int receieveTime = log.get(1);
                if (queryElement - X <= receieveTime && receieveTime <= queryElement) {
                    receieved.add(log.get(0));
                }
            }
            result.add(n - receieved.size());
        }
        return result;
    }

    public static void main(String[] args) {
//        ArrayList<List<String>> products = new ArrayList<>();
//
//        ArrayList<String> list1 = new ArrayList<>();
//        products.add(Lists.newArrayList("10","d0","D1"));
//        products.add(Lists.newArrayList("15","empty","empty"));
//        products.add(Lists.newArrayList("20","D1","empty"));
//
//
//        List<List<String>> discounts = new ArrayList<>();
//        discounts.add(Lists.newArrayList("d0","1","54"));
//        discounts.add(Lists.newArrayList("D1","2","5"));
//        System.out.println(findLowestPrice1(products, discounts));
//        System.out.println(findLowestPrice(products, discounts));

        Integer[] arr={5,2,6,1,3, null,7};
        List<Integer> res = listRightElement(arr);
        System.out.println(res);
        String s="select lg_pk, lg_user,lg_message,lg_time from logs logs1 join logs logs2 on logs1.lg_pk=logs2.lg_pk-1 " +
                "where logs1.lg_user=logs2.lg_user and logs1.lg_message=logs2.lg_message";
        String s1="select lg_user,lg_message from logs group by lg_user,lg_message having";
    }

    public static int findLowestPrice1(List<List<String>> products, List<List<String>> discounts) {
        int lowestPrice = 0;


        Map<String, List<String>> tag2DiscountMap = new HashMap<>();
        for (List<String> discount : discounts) {
            tag2DiscountMap.putIfAbsent(discount.get(0), discount);
        }
        for (List<String> product : products) {
            Double price = Double.parseDouble(product.get(0));

            if (product.size() == 1) {
                lowestPrice += price;
                continue;
            }

            int finalPrice = Integer.MAX_VALUE;

            for (int i = 1; i < product.size(); i++) {
                String tag = product.get(i);
                List<String> discount = tag2DiscountMap.get(tag);
                if (discount == null) {
                    continue;
                }
                String type = discount.get(1);
                Double value = Double.parseDouble(discount.get(2));

                switch (type) {
                    case "0":
                        finalPrice = Math.min(finalPrice, (int) Math.round(value));
                        break;
                    case "1":
                        double result = price - price * value / 100;
                        int roundedResult = (int) Math.round(result);
                        finalPrice = Math.min(finalPrice, roundedResult);
                        break;
                    case "2":
                        finalPrice = Math.min(finalPrice, (int) Math.round(price-value));
                        break;
                    default:
                        break;
                }
            }
            finalPrice = Math.min(finalPrice, (int) Math.round(price));
            lowestPrice += finalPrice;
        }
        return lowestPrice;
    }

    public static int findLowestPrice(List<List<String>> products, List<List<String>> discounts) {
        // Write your code here

        int lowestPrice = 0;

        HashMap<String, List<List<String>>> tag2DiscountMap = new HashMap<>();
        for (List<String> discount : discounts) {
            List<List<String>> value = tag2DiscountMap.get(discount.get(0));
            if (value != null) {
                value.add(discount);
            } else {
                value = new ArrayList<>();
                value.add(discount);
                tag2DiscountMap.put(discount.get(0), value);
            }
        }

        for (List<String> product : products) {
            Integer price = Integer.valueOf(product.get(0));

            if (product.size() == 1) {
                lowestPrice += price;
                continue;
            }
            PriorityQueue<Integer> priceAfterDiscount = new PriorityQueue<>();
            for (int i = 1; i < product.size(); i++) {
                String tag = product.get(i);
                List<List<String>> value = tag2DiscountMap.get(tag);
                if (value == null) {
                    priceAfterDiscount.add(price);
                    continue;
                }
                for (List<String> discount : value) {
                    String type = discount.get(1);
                    String amountStr = discount.get(2);
                    if (type != null && amountStr != null) {
                        Integer amount = Integer.valueOf(amountStr);
                        switch (type) {
                            case "0":
                                priceAfterDiscount.add(amount);
                                break;
                            case "1":
                                double result = price - price * (double) amount / 100;
                                int roundedResult = (int) Math.round(result);
                                priceAfterDiscount.add(roundedResult);
                                break;
                            case "2":
                                priceAfterDiscount.add(price - amount);
                                break;
                            default:
                                priceAfterDiscount.add(price);
                                break;
                        }
                    } else {
                        priceAfterDiscount.add(price);
                    }
                }

            }
            lowestPrice += priceAfterDiscount.peek();
        }
        return lowestPrice;
    }

    public static int roundOff(int price, int amount) {
        double result = price - price * (double) amount / 100;
        int roundedResult = (int) Math.round(result);
        return roundedResult;
    }

//    public static void main(String[] args) {
//        int i = roundOff(50, 15);
//        System.out.println(i);
//        List<Integer> result = getTimes(Arrays.asList(0, 0, 1, 5), Arrays.asList(0, 1, 1, 0));
//        for (int i : result) {
//            System.out.println(i);
//        }
//    }


    public static List<Integer> getTimes(List<Integer> time, List<Integer> direction) {
        // Write your code here
        PriorityQueue<Element> enterQueue = new PriorityQueue<>((a, b) -> a.time == b.time ? a.index - b.index : a.time - b.time);
        PriorityQueue<Element> exitQueue = new PriorityQueue<>((a, b) -> a.time == b.time ? a.index - b.index : a.time - b.time);

        boolean exit = true;
        int n = time.size();
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            if (direction.get(i) == 0) {
                enterQueue.offer(new Element(time.get(i), direction.get(i), i));
            } else {
                exitQueue.offer(new Element(time.get(i), direction.get(i), i));
            }

        }

        int currentTime = 0;
        while (!enterQueue.isEmpty() || !exitQueue.isEmpty()) {
            Element poll = null;
            if (!enterQueue.isEmpty() && !exitQueue.isEmpty()) {
                Element peek = enterQueue.peek();
                Element peek1 = exitQueue.peek();
                if (peek.time <= currentTime && peek1.time <= currentTime) {
                    if (exit) {
                        poll = exitQueue.poll();
                    } else {
                        poll = enterQueue.poll();
                    }
                } else if (peek.time <= currentTime) {
                    poll = enterQueue.poll();
                } else {
                    poll = exitQueue.poll();
                }
            } else if (!enterQueue.isEmpty()) {
                poll = enterQueue.poll();
            } else {
                poll = exitQueue.poll();
            }
            currentTime = Math.max(poll.time, currentTime);
            result[poll.index] = currentTime;
            if (poll.direction == 0) {
                exit = false;
            } else {
                exit = true;
            }
            currentTime++;
        }
        ArrayList<Integer> res = new ArrayList<>();
        for (int i : result) {
            res.add(i);
        }
        return res;

    }

    static class Element {
        int time;
        int direction;
        int index;

        public Element(int t, int d, int i) {
            this.time = t;
            this.direction = d;
            this.index = i;
        }
    }

//    public static void main(String[] args) {
////        int maxGold = getMaxGold(4, 5, 7);
////        System.out.println(maxGold);
////
////        Scanner in = new Scanner(System.in);
////
////        while (in.hasNext()) {
////            int m = in.nextInt();
////            int n = in.nextInt();
////            int limit = in.nextInt();
////
////            System.out.println(getMaxGold(m, n, limit));
////        }
//        int minutes = getMinutes(4, new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 3);
//        System.out.println(minutes);
//    }

    public static int max(String s) {
        char[] chars = s.toCharArray();
        char start = 0;
        int count = 0;
        int res = 0;
        for (int i = 0; i < chars.length; i++) {
            if (start == 0) {
                start = chars[i];
                count++;
            } else if (start == chars[i]) {
                count++;
            } else {
                count--;
                if (count == 0) {
                    start = 0;
                    res++;
                }
            }
        }
        return res;
    }

    public static int getMaxGold(int m, int n, int limit) {
        int[][] map = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (getSum(i) + getSum(j) <= limit) {
                    map[i][j] = 1;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        getMaxGold(map, 0, 0);
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (map[i][j] == 2) {
                    res++;
                }
            }
        }
        return res;
    }

    public static int getSum(int num) {
        int total = 0;
        while (num > 0) {
            total += num % 10;
            num /= 10;
        }
        return total;
    }

    public static void getMaxGold(int[][] map, int i, int j) {
        if (i < 0 || j < 0 || i >= map.length || j >= map[0].length) {
            return;
        }
        if (map[i][j] == 0 || map[i][j] == 2) {
            return;
        }
        map[i][j] = 2;
        getMaxGold(map, i + 1, j);
        getMaxGold(map, i - 1, j);
        getMaxGold(map, i, j + 1);
        getMaxGold(map, i, j - 1);
    }


    public static int getMinutes(int n, int[][] path, int startId) {
        HashMap<Integer, List<WillBadComputer>> map = new HashMap<>();

        for (int i = 0; i < path.length; i++) {
            map.computeIfAbsent(path[i][0], k -> new ArrayList<>()).add(new WillBadComputer(path[i][1], path[i][2]));
        }
        int minute = 0;
        HashSet<Integer> set = new HashSet<>();
        set.add(startId);
        PriorityQueue<WillBadComputer> queue = new PriorityQueue<>(Comparator.comparing(WillBadComputer::getInfectTime));
        List<WillBadComputer> willBadComputers = map.get(startId);
        for (WillBadComputer willBadComputer : willBadComputers) {
            willBadComputer.infectTime = willBadComputer.time;
            queue.offer(willBadComputer);
            set.add(willBadComputer.id);
        }

        while (!queue.isEmpty()) {
            while (!queue.isEmpty()) {
                WillBadComputer peek = queue.peek();
                if (peek.infectTime == minute) {
                    WillBadComputer poll = queue.poll();
                    List<WillBadComputer> willBadComputers1 = map.get(poll.id);
                    if (willBadComputers1 != null) {
                        for (WillBadComputer willBadComputer : willBadComputers1) {
                            willBadComputer.infectTime = willBadComputer.time + minute;
                            queue.offer(willBadComputer);
                            set.add(willBadComputer.id);
                        }
                    }
                } else {
                    break;
                }
            }
            minute++;
        }
        return set.size() == n ? minute : -1;
    }

    static class WillBadComputer {
        int id;
        int time;
        int infectTime;

        public WillBadComputer(int id, int time) {
            this.id = id;
            this.time = time;
        }

        public int getInfectTime() {
            return infectTime;
        }
    }


    public static List<Integer> listRightElement(Integer[] arr){
        List<Integer> res=new ArrayList<>();
        int index=0;
        while(index< arr.length){
            res.add(arr[index]);
            index=(index+1)*2;
        }
        return res;
    }


}
