package cn.zswltech.easyexcel.demo.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO
 *
 * @author wangchuanhao
 * @date 2023/5/15 7:29 PM
 */
public class MainClass3 {

    public static void main(String[] args) {
        List<Integer> result = getTimes(Arrays.asList(0, 0, 1, 5), Arrays.asList(0, 1, 1, 0));
        for (int i : result) {
            System.out.println(i);
        }
    }

    public static List<Integer> getTimes(List<Integer> time, List<Integer> direction) {
        int a = findNext(direction, -1 ,0), b = findNext(direction, -1 ,1);
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; ; i++) {
            if (list.size() != 0 && list.get(list.size() - 1).get(1) == i - 1 && direction.get(list.get(list.size() - 1).get(0)) == 0) {
                if (a < time.size() && time.get(a) <= i) {
                    list.add(Arrays.asList(a, i));
                    a = findNext(direction, a ,0);
                    continue;
                }
                if (b < time.size() && time.get(b) <= i) {
                    list.add(Arrays.asList(b, i));
                    b = findNext(direction, b ,1);
                    continue;
                }
            } else {
                if (b < time.size() && time.get(b) <= i) {
                    list.add(Arrays.asList(b, i));
                    b = findNext(direction, b ,1);
                    continue;
                }
                if (a < time.size() && time.get(a) <= i) {
                    list.add(Arrays.asList(a, i));
                    a = findNext(direction, a ,0);
                    continue;
                }
            }
            if (a >= time.size() && b >= time.size()) {
                break;
            }
            // 这里这个优化可以考虑抄不抄，不抄就会有部分case超时 这里开始
            int jumpA = Integer.MAX_VALUE, jumpB = Integer.MAX_VALUE;
            if (a < time.size()) {
                jumpA = time.get(a);
            }
            if (b < time.size()) {
                jumpB = time.get(b);
            }
            if ((jumpA == Integer.MAX_VALUE || jumpA > i)
                    && (jumpB == Integer.MAX_VALUE || jumpB > i)) {
                i = Math.min(jumpA, jumpB) - 1;
            }
            // 这里这个优化可以考虑抄不抄，不抄就会有部分case超时 这里结束
        }
        List<Integer> result = list.stream().sorted(Comparator.comparingInt(s -> s.get(0))).map(s -> s.get(1)).collect(Collectors.toList());
        return result;
    }

    public static int findNext(List<Integer> direction, int pi, int d) {
        for (int i = pi + 1; i < direction.size(); i++) {
            if (direction.get(i) == d) {
                return i;
            }
        }
        return direction.size();
    }

}
