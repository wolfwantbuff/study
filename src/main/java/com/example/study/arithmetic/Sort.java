package com.example.study.arithmetic;

import java.util.PriorityQueue;

/**
 * @author xiaodong
 * @date 2023/11/1 16:19
 * @wiki
 */
public class Sort {
    public static int[] merge(int[] nums1, int m, int[] nums2, int n) {
        int[] res = new int[m + n];
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int i = 0; i < m; i++) {
            queue.add(nums1[i]);
        }
        for (int j = 0; j < n; j++) {
            queue.add(nums2[j]);
        }
        for (int k = 0; k < res.length; k++) {
            res[k] = (int) queue.poll();
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 0, 0, 0};
        int[] arr2 = {2, 5, 6};
        merge(arr1, 3, arr2, 3);
        int[] arr = {64, 34, 25, 12, 22, 11, 90, 13, 134, 24, 99, 74, 10, 94, 76, 20, 35, 76, 19};
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void selectionSort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (arr[minIndex] > arr[j]) {
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }
    }

    public static void bubbleSort(int[] arr) {
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    // 堆调整
    public static void minHeapify(int[] arr, int i, int len) {
        int leftChild = i * 2 + 1;
        int rightChild = leftChild + 1;
        int min = i;
        if (leftChild <= len && arr[min] > arr[leftChild]) {
            min = leftChild;
        }
        if (rightChild <= len && arr[min] > arr[rightChild]) {
            min = rightChild;
        }
        if (min != i) {
            swap(arr, i, min);
            minHeapify(arr, min, len);
        }
    }

    public static void heapSort(int[] arr) {
        // 建立小顶堆
        int len = arr.length - 1;
        int start = (len - 1) / 2;
        for (int i = start; i >= 0; i--) {
            minHeapify(arr, i, len);
        }

        for (int j = len; j > 0; j--) {
            swap(arr, 0, j);
            minHeapify(arr, 0, j - 1);
        }
    }

    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) return;
        int left = start;
        int right = end;
        int ref = arr[left];
        while (left < right) {
            while (left < right && arr[right] > ref) {
                right--;
            }
            if (left < right) {
                arr[left] = arr[right];
                left++;
            }

            while (left < right && arr[left] < ref) {
                left++;
            }
            if (left < right) {
                arr[right] = arr[left];
                right--;
            }
        }
        arr[left] = ref;
        quickSort(arr, start, left - 1);
        quickSort(arr, left + 1, end);
    }

}
