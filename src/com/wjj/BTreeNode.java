package com.wjj;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * BTree结点定义
 *
 * @param <T> 泛型类型
 * @author Aldebran
 * @since 17/10/2020
 */
public class BTreeNode<T> {

    public T[] keys;

    public BTreeNode[] childs;

    public int length;

    public BTreeNode<T> parent;

    public int parentIndex;

    public BTreeNode(int m, Class<T> cls) {
        keys = (T[]) Array.newInstance(cls, m);
        childs = (BTreeNode[]) Array.newInstance(BTreeNode.class, m + 1);
        length = 0;
        parent = null;
        parentIndex = -1;
    }

    public static void setRelationship(BTreeNode p, BTreeNode c, int index) {
        if (c != null) {
            c.parent = p;
            c.parentIndex = index;
        }
        if (p != null) {
            p.childs[index] = c;
        }
    }

    public static void insert(Object[] array, Object obj, int index, int length) {
        for (int i = length - 1; i >= index; i--) {
            array[i + 1] = array[i];
        }
        array[index] = obj;
    }

    @Override
    public String toString() {
        return "BTreeNode{" +
                "keys=" + Arrays.toString(keys) +
                '}';
    }
}
