package com.morphology;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

class TreeNode<T> {
    public T value;
    public TreeNode<T> left;
    public TreeNode<T> right;
    public TreeNode(T value) {
        this.value = value;
    }
}
public class BinaryTree {
    /**
     * 前序遍历（递归方式）
     * @param node
     */
    public static void preOrderByRecursion(TreeNode node) {
        if (node != null) {
            System.out.print(node.value);
            preOrderByRecursion(node.left);
            preOrderByRecursion(node.right);
        }
    }

    /**
     * 前序遍历（非递归方式）
     * 1.先入栈根结点，输出根结点value值，再先后入栈其右结点、左结点；
     * 2.出栈左结点，输出其value值，再入栈该左结点的右结点、左结点；直到遍历完该左结点所在子树。
     * 3.再出栈右结点，输出其value值，再入栈该右结点的右结点、左结点；直到遍历完该右结点所在子树。
     * @param root
     */
    public static void preOrder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        if (root != null) {
            stack.push(root);
        }

        while (!stack.isEmpty()) {
            TreeNode top = stack.pop();
            System.out.print(top.value);
            if (top.right != null) {
                stack.push(top.right);
            }
            if (top.left != null) {
                stack.push(top.left);
            }
        }
    }

    /**
     * 中序遍历（递归方式）
     * @param node
     */
    public static void inOrderByRecursion(TreeNode node) {
        if (node != null) {
            inOrderByRecursion(node.left);
            System.out.print(node.value);
            inOrderByRecursion(node.right);
        }
    }

    /**
     * 中序遍历（非递归方式）
     * 1.首先从根结点出发一路向左，入栈所有的左结点；
     * 2.出栈一个结点，输出该结点value值，查询该结点是否存在右结点，
     * 若存在则从该右结点出发一路向左入栈该右结点所在子树所有的左结点；
     * 3.若不存在右结点，则出栈下一个结点，输出结点value值，同步骤2操作；
     * 4.直到结点为null，且栈为空。
     * @param root
     */
    public static void inOrder(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {
                TreeNode top = stack.pop();
                System.out.print(top.value);
                root = top.right;
            }
        }
    }

    /**
     * 后序遍历（递归方式）
     * @param node
     */
    public static void postOrderByRecursion(TreeNode node) {
        if (node != null) {
            postOrderByRecursion(node.left);
            postOrderByRecursion(node.right);
            System.out.print(node.value);
        }
    }

    /**
     * 后序遍历（非递归）
     * 1.首先定义两个stack，将root结点压入stack1
     * 2.stack1弹出栈顶元素，然后将该元素压入stack2，再将该元素的左结点与右结点压入stack1
     * 3.循环步骤2，直到stack1为空，根据栈的LIFO的特性，这样遍历Stack2就会得到后序遍历的结果
     * @param root
     */
    public static void postOrder(TreeNode root) {
        if (root != null) {
            Stack<TreeNode> stack1 = new Stack<>();
            Stack<TreeNode> stack2 = new Stack<>();
            stack1.push(root);
            while (!stack1.isEmpty()) {
                TreeNode top = stack1.pop();
                stack2.push(top);
                if (top.left != null) {
                    stack1.push(top.left);
                }
                if (top.right != null) {
                    stack1.push(top.right);
                }
            }
            while (!stack2.isEmpty()) {
                System.out.print(stack2.pop().value);
            }
        }
    }

    /**
     * 层次遍历
     * @param root
     */
    public static void layerOrder(TreeNode root) {
        Queue<TreeNode> queue = new ArrayDeque<>();
        if (root != null) {
            queue.offer(root);
        }
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.value);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }
    /**
     * 求最大深度（递归）
     * @param root
     * @return
     */
    public static int maxDepthByRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = maxDepthByRecursion(root.left);
        int right = maxDepthByRecursion(root.right);
        return Math.max(left, right) + 1;
    }

    /**
     * 求最大深度（非递归）
     * @param root
     * @return
     */
    public static int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 结点不断入队出队的过程，直到左右都无叶子结点
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            level ++;
            int levelNum = queue.size();
            for (int i = 0; i < levelNum; i++) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return level;
    }

    /**
     * 求最小深度（递归）
     * @param root
     * @return
     */
    public static int minDepthByRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // 无叶子结点的情况
        if (root.left == null && root.right == null) {
            return 1;
        }

        // 左子结点为空的情况
        if (root.left == null && root.right != null) {
            return minDepthByRecursion(root.right) + 1;
        }

        // 右子结点为空的情况
        if (root.left != null && root.right == null) {
            return minDepthByRecursion(root.left) + 1;
        }

        int left = minDepthByRecursion(root.left);
        int right = minDepthByRecursion(root.right);

        return Math.min(left, right) + 1;
    }

    /**
     * 求最小深度（非递归）
     * @param root
     * @return
     */
    public static int minDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            level ++;
            int levelNum = queue.size();
            for (int i = 0; i < levelNum; i++) {
                TreeNode node = queue.poll();
                // 当出现第一个无叶子结点的结点时，该结点的深度为最小深度
                if (node.left == null && node.right == null) {
                    return level;
                }
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return level;
    }

}


