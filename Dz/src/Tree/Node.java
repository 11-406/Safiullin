package Tree;

public class Node {
    private Node left;
    private Node right;
    private int data;

    public Node(int data) {
        this.data = data;
        right = null;
        left = null;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getData() {
        return data;
    }


}
