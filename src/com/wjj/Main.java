package com.wjj;

import java.util.*;

/**
 * hashMap、treeMap和BTree查询效率对比测试
 *
 * @author Aldebran
 * @since 17/10/2020
 */
public class Main {

    // 测试数组长度
    public static final int length = 10000000;

    // B树阶数
    public static final int m = 512;

    // 测试次数
    public static final int time = 10;

    // 生成随机数组
    public static int[] buildArray(int length) {
        int[] a = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            a[i] = random.nextInt(length * 10);
        }
        return a;
    }

    // 测试从Map中查询
    public static long testGetMap(Map<Integer, Integer> map, int[] array) {
        long st = System.currentTimeMillis();
        for (int t = 0; t < time; t++) {
            for (int i = 0; i < length; i++) {
                Integer n = map.get(array[i]);
                if (n == null || n.intValue() != array[i]) {
                    // 查询失败
                    throw new RuntimeException();
                }
            }
        }
        return (System.currentTimeMillis() - st) / time;
    }

    // 测试从BTree查询
    public static long testGetBTree(BTree<Integer> bTree, int[] array) {
        long st = System.currentTimeMillis();
        for (int t = 0; t < time; t++) {
            for (int i = 0; i < length; i++) {
                Integer n = bTree.get(array[i]);
                if (n == null || n.intValue() != array[i]) {
                    // 查询失败
                    throw new RuntimeException();
                }
            }
        }
        return (System.currentTimeMillis() - st) / time;
    }

    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        Map<Integer, Integer> treeMap = new TreeMap<>();
        BTree<Integer> bTree = new BTree<>(m, Integer.class);
        // 插入
        int[] array = buildArray(length);
        Arrays.stream(array).forEach(n -> {
            hashMap.put(n, n);
            treeMap.put(n, n);
            bTree.insert(n);
        });
        System.out.println("hashMap: " + testGetMap(hashMap, array));
        System.out.println("treeMap: " + testGetMap(treeMap, array));
        System.out.println("BTree: " + testGetBTree(bTree, array));
    }


}
