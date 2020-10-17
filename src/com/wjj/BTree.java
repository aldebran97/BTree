package com.wjj;

import java.util.Comparator;

/**
 * B树
 *
 * @param <T> 泛型类型
 * @author Aldebran
 * @since 17/10/2020
 */
public class BTree<T> {

    private Comparator<T> comparator;

    private BTreeNode<T> root;

    private int m;

    private Class<T> cls;

    public BTree(int m, Class<T> cls) {
        this.m = m;
        this.cls = cls;
    }

    public BTree(int m, Class<T> cls, Comparator<T> comparator) {
        this(m, cls);
        this.comparator = comparator;
    }

    private int compare(T v1, T v2) {
        if (comparator != null) {
            return comparator.compare(v1, v2);
        } else {
            Comparable c1 = (Comparable) v1;
            return c1.compareTo(v2);
        }
    }

    /**
     * 二分查找元素或分路号
     */
    private SearchResult search(BTreeNode<T> node, T elem) {
        SearchResult searchResult = new SearchResult();
        int low = 0, high = node.length - 1, mid = -1;
        while (low <= high) {
            mid = (low + high) / 2;
            int cmp = compare(elem, node.keys[mid]);
            if (cmp == 0) {
                searchResult.index = mid;
                searchResult.search = true;
                return searchResult;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        int cmp = compare(elem, node.keys[mid]);
        if (cmp < 0) {
            searchResult.index = mid;
        } else {
            searchResult.index = mid + 1;
        }
        return searchResult;
    }

    /**
     * 查找元素
     */
    public T get(T elem) {
        BTreeNode<T> current = root;
        while (current != null) {
            SearchResult searchResult = search(current, elem);
            if (searchResult.search) {
                return current.keys[searchResult.index];
            } else {
                current = current.childs[searchResult.index];
            }
        }
        return null;
    }

    /**
     * 插入元素
     */
    public void insert(T elem) {
        if (root == null) {
            root = new BTreeNode<>(m, cls);
            root.length = 1;
            root.keys[0] = elem;
            return;
        }
        BTreeNode<T> current = root;
        BTreeNode<T> insertNode = null;
        int insertIndex = -1;
        while (current != null) {
            SearchResult searchResult = search(current, elem);
            if (searchResult.search) {
                return;
            } else {
                insertNode = current;
                insertIndex = searchResult.index;
                current = current.childs[searchResult.index];
            }
        }
        if (insertNode == null) {
            throw new RuntimeException();
        }
        BTreeNode.insert(insertNode.keys, elem, insertIndex, insertNode.length);
        insertNode.length++;
        fission(insertNode);
    }

    /**
     * 向上分裂
     */
    private void fission(BTreeNode<T> node) {
        if (node.length < m) {
            return;
        }
        int mid = m / 2;
        BTreeNode<T> node1 = new BTreeNode<>(m, cls);
        BTreeNode<T> node2 = new BTreeNode<>(m, cls);
        System.arraycopy(node.keys, 0, node1.keys, 0, mid);
        System.arraycopy(node.childs, 0, node1.childs, 0, mid + 1);
        for (int i = 0; i < mid + 1; i++) {
            BTreeNode.setRelationship(node1, node1.childs[i], i);
        }
        node1.length = mid;
        System.arraycopy(node.keys, mid + 1, node2.keys, 0, node.length - 1 - mid);
        System.arraycopy(node.childs, mid + 1, node2.childs, 0, m - mid);
        for (int i = 0; i < m - mid; i++) {
            BTreeNode.setRelationship(node2, node2.childs[i], i);
        }
        node2.length = node.length - 1 - mid;
        if (node.parent == null) {
            BTreeNode<T> newRoot = new BTreeNode<>(m, cls);
            newRoot.length = 1;
            newRoot.keys[0] = node.keys[mid];
            BTreeNode.setRelationship(newRoot, node1, 0);
            BTreeNode.setRelationship(newRoot, node2, 1);
            root = newRoot;
            return;
        }
        BTreeNode<T> p = node.parent;
        for (int childIndex = p.length; childIndex >= node.parentIndex + 1; childIndex--) {
            BTreeNode.setRelationship(p, p.childs[childIndex], childIndex + 1);
        }
        BTreeNode.insert(p.keys, node.keys[mid], node.parentIndex, p.length);
        BTreeNode.setRelationship(p, node1, node.parentIndex);
        BTreeNode.setRelationship(p, node2, node.parentIndex + 1);
        p.length++;
        fission(p);
    }
}
