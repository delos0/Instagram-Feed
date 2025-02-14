import java.util.ArrayList;
import java.util.Comparator;

public class AVL<E extends Comparable<E>> {

    private Node<E> root;
    private long size = 0;

    public AVL() {}

    public void insert(E element) {
        root = insert(element, root);
    }

    // Recursively insert an element and balance the tree
    private Node<E> insert(E element, Node<E> current) {
        if (current == null) return newNode(element);

        if (element.compareTo(current.element) < 0) {
            current.left = insert(element, current.left);
        } else if (element.compareTo(current.element) > 0) {
            current.right = insert(element, current.right);
        }

        size++;
        return balance(current); // Balance the tree after insertion
    }

    public void delete(E element) {
        root = delete(element, root);
    }

    // Recursively delete an element and balance the tree
    private Node<E> delete(E element, Node<E> current) {
        if (current == null) return null;

        if (element.compareTo(current.element) == 0) {
            // Handle leaf and single-child cases
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            // Replace with the smallest value in the right subtree
            E min = findMin(current.right);
            current.element = min;
            current.right = delete(min, current.right);
        } else if (element.compareTo(current.element) < 0) {
            current.left = delete(element, current.left);
        } else {
            current.right = delete(element, current.right);
        }

        return balance(current); // Balance the tree after deletion
    }

    // Find the minimum element in a subtree
    private E findMin(Node<E> root) {
        if (root.left == null) return root.element;
        return findMin(root.left);
    }

    protected Node<E> newNode(E e) {
        return new Node<>(e);
    }

    public Node<E> getRoot() {
        return root;
    }

    public Node<E> search(E element) {
        Node<E> current = root;
        while (current != null) {
            if (element.equals(current.element)) return current;
            if (element.compareTo(current.element) < 0) current = current.left;
            else current = current.right;
        }
        return null;
    }

    // In-order traversal of the tree, collecting nodes until a target
    public void inorder(Node<E> root, ArrayList<Node<E>> nodes, Node<E> target) {
        if (root == null) return;
        inorder(root.left, nodes, target);
        if (root.element.compareTo(target.element) >= 0) return;
        nodes.add(root);
        inorder(root.right, nodes, target);
    }

    // Traverse the tree and add elements to an ArrayList in sorted order
    public void insertToArray(Node<E> root, ArrayList<E> array) {
        if (root == null) return;
        insertToArray(root.left, array);
        array.add(root.element);
        insertToArray(root.right, array);
    }

    // Insert elements of the tree into another AVL tree
    public void insertToTree(Node<E> root, AVL<E> tree) {
        if (root == null) return;
        insertToTree(root.left, tree);
        tree.insert(root.element);
        insertToTree(root.right, tree);
    }

    // In-order traversal of nodes greater than a target
    public void inorderRight(Node<E> root, ArrayList<Node<E>> nodes, Node<E> target) {
        if (root == null) return;
        inorderRight(root.right, nodes, target);
        if (root.element.compareTo(target.element) <= 0) return;
        nodes.add(root);
        inorderRight(root.left, nodes, target);
    }

    public boolean isEmpty() {
        return root == null;
    }

    private int getHeight(Node<E> node) {
        return node == null ? -1 : node.height;
    }

    private int getMax(int first, int second) {
        return Math.max(first, second);
    }

    // Balance the tree at a given node
    private Node<E> balance(Node<E> node) {
        updateHeight(node);
        int balanceFactor = getBalanceFactor(node);

        // Right-heavy case
        if (balanceFactor > 1) {
            if (getBalanceFactor(node.right) < 0) node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        // Left-heavy case
        if (balanceFactor < -1) {
            if (getBalanceFactor(node.left) > 0) node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        return node;
    }

    private Node<E> rotateLeft(Node<E> node) {
        Node<E> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    private Node<E> rotateRight(Node<E> node) {
        Node<E> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    private void updateHeight(Node<E> node) {
        node.height = 1 + getMax(getHeight(node.left), getHeight(node.right));
    }

    private int getBalanceFactor(Node<E> node) {
        return node == null ? 0 : getHeight(node.right) - getHeight(node.left);
    }
}
