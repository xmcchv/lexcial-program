package com.morphology;

import java.util.HashMap;
import java.util.Map;

    /**
     * 问题：
     * 输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。
     * 假设输入的前序遍历和中序遍历结果中都不含重复的数字。
     * 例如输入:
     * 前序遍历序列{"A","B","D","H","I","E","J","C","F","K","G"}，
     * 中序遍历序列{"H","D","I","B","E","J","A","F","K","C","G"}，
     * 则重建出二叉树并输出他的根结点。
     *
     * 解析：
     * 在二叉树的前序遍历中，第一个数字总是树的根节点。在中序遍历中，树的根节点在序列的中间，
     * 左子树的节点的值位于根节点的左边，右子树节点的值位于根节点值的右边。
     *
     * 因此需要扫描中序遍历序列才能找到根结点的值，由此可以找到左子树的节点的个数和右子树节点的个数，
     * 然后在前序遍历序列中找到左子树的根节点，再到中序遍历序列中找到左子树的左子树和右子树。依次递归。
     * 由于二叉树的构造本身就是用递归实现的，所以重建二叉树也用递归进行实现实很简单的。
     */
    public class BuildBinaryTree {

        public static TreeNode buildTree(String[] preOrder, String[] inOrder) {
            if (preOrder == null || inOrder == null) {
                return null;
            }
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < inOrder.length; i++) {
                map.put(inOrder[i], i);
            }

            return buildTree(preOrder, 0, preOrder.length - 1, inOrder, 0, inOrder.length - 1, map);

        }

        public static TreeNode buildTree(String[] preOrder, int pstart, int pend,
                                         String[] inOrder, int istart, int iend, Map<String, Integer> map) {
            if (pstart > pend || istart > iend) {
                return null;
            }
            TreeNode head = new TreeNode(preOrder[pstart]);
            int index = map.get(preOrder[pstart]);

            head.left = buildTree(preOrder, pstart + 1, pstart + index - istart, inOrder, istart, index - 1, map);
            head.right = buildTree(preOrder, pstart + index - istart + 1, pend, inOrder, index + 1, iend, map);

            return head;
        }

        public static void main(String[] args) {
            String[] preOrder = {"A", "B", "D", "H", "I", "E", "J", "C", "F", "K", "G"};
            String[] inOrder = {"H", "D", "I", "B", "E", "J", "A", "F", "K", "C", "G"};

            TreeNode head = buildTree(preOrder, inOrder);
            // 前序遍历(递归)
            System.out.print("前序遍历：");
            BinaryTree.preOrderByRecursion(head);
            System.out.println();

            // 中序遍历(递归)
            System.out.print("中序遍历：");
            BinaryTree.inOrderByRecursion(head);
            System.out.println();

            // 后序遍历(递归)
            System.out.print("后序遍历：");
            BinaryTree.postOrderByRecursion(head);
            System.out.println();

            // 层次遍历
            System.out.print("层次遍历：");
            BinaryTree.layerOrder(head);
        }
    }

