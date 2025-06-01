import java.util.*;

public class FibonacciHeap {
    private Node min;
    private int size;
    private int operationCount;

    private static class Node {
        int key;
        int degree;
        Node parent, child, left, right;
        boolean mark;

        Node(int key) {
            this.key = key;
            this.left = this.right = this;
        }
    }

    public void insert(int key) {
        operationCount++; // создание нового узла
        Node node = new Node(key);
        operationCount++; // слияние в корневой список
        min = mergeLists(min, node);
        size++;
        operationCount++; // увеличение размера
    }

    public Integer findMin() {
        operationCount++; // одна операция получения минимума
        return (min != null) ? min.key : null;
    }

    public boolean contains(int key) {
        resetOperationCount();
        Set<Node> visited = new HashSet<>();
        return find(min, key, visited) != null;
    }

    private Node find(Node start, int key, Set<Node> visited) {
        if (start == null || visited.contains(start)) return null;

        Node current = start;
        do {
            operationCount++; // просмотр узла
            if (current.key == key) return current;

            visited.add(current);
            Node result = find(current.child, key, visited);
            if (result != null) return result;

            current = current.right;
        } while (current != start && !visited.contains(current));

        return null;
    }

    public boolean delete(int key) {
        resetOperationCount();
        Set<Node> visited = new HashSet<>();
        Node node = find(min, key, visited);
        if (node == null) return false;

        operationCount++; // уменьшение ключа
        decreaseKey(node, Integer.MIN_VALUE);
        operationCount++; // удаление минимума
        extractMin();
        return true;
    }

    private void decreaseKey(Node node, int newKey) {
        operationCount++;
        if (newKey > node.key) throw new IllegalArgumentException("new key is greater");
        node.key = newKey;

        Node parent = node.parent;
        if (parent != null && node.key < parent.key) {
            cut(node, parent);
            cascadeCut(parent);
        }

        if (node.key < min.key) {
            min = node;
        }
    }

    private void cut(Node node, Node parent) {
        operationCount++; // отрезание узла
        removeChild(parent, node);
        parent.degree--;
        min = mergeLists(min, node);
        node.parent = null;
        node.mark = false;
    }

    private void cascadeCut(Node node) {
        Node parent = node.parent;
        if (parent != null) {
            if (!node.mark) {
                node.mark = true;
                operationCount++; // установка метки
            } else {
                cut(node, parent);
                cascadeCut(parent);
            }
        }
    }

    private void removeChild(Node parent, Node child) {
        operationCount++; // удаление потомка
        if (child.right == child) {
            parent.child = null;
        } else {
            if (parent.child == child) parent.child = child.right;
            child.left.right = child.right;
            child.right.left = child.left;
        }
    }

    public Integer extractMin() {
        operationCount++; // начало удаления минимума
        if (min == null) return null;

        Node oldMin = min;
        if (min.child != null) {
            Node child = min.child;
            List<Node> children = new ArrayList<>();
            Set<Node> visited = new HashSet<>();

            do {
                if (!visited.contains(child)) {
                    visited.add(child);
                    children.add(child);
                    child = child.right;
                } else break;
            } while (child != min.child);

            for (Node c : children) {
                c.parent = null;
                operationCount++; // добавление ребёнка в корневой список
                min = mergeLists(min, c);
            }
        }

        removeNode(min);
        size--;

        if (size == 0) {
            min = null;
        } else {
            min = oldMin.right;
            consolidate();
        }

        return oldMin.key;
    }

    private void consolidate() {
        int arraySize = ((int) Math.floor(Math.log(size) / Math.log(2))) + 1;
        Node[] degreeTable = new Node[arraySize];
        List<Node> rootList = new ArrayList<>();
        Set<Node> visited = new HashSet<>();

        Node current = min;
        while (current != null && !visited.contains(current)) {
            rootList.add(current);
            visited.add(current);
            current = current.right;
        }

        for (Node node : rootList) {
            int d = node.degree;
            while (degreeTable[d] != null) {
                operationCount++; // слияние деревьев одной степени
                Node other = degreeTable[d];
                if (node.key > other.key) {
                    Node temp = node;
                    node = other;
                    other = temp;
                }
                link(other, node);
                degreeTable[d] = null;
                d++;
            }
            degreeTable[d] = node;
        }

        min = null;
        for (Node node : degreeTable) {
            if (node != null) {
                operationCount++; // вставка в новый корневой список
                min = mergeLists(min, node);
            }
        }
    }

    private void link(Node y, Node x) {
        removeNode(y);
        y.left = y.right = y;
        x.child = mergeLists(x.child, y);
        y.parent = x;
        x.degree++;
        y.mark = false;
        operationCount++; // привязка y к x
    }

    private void removeNode(Node node) {
        node.left.right = node.right;
        node.right.left = node.left;
        operationCount++; // удаление узла из списка
    }

    private Node mergeLists(Node a, Node b) {
        if (a == null) return b;
        if (b == null) return a;

        Node temp = a.right;
        a.right = b.right;
        a.right.left = a;
        b.right = temp;
        b.right.left = b;

        operationCount++; // слияние двух списков
        return a.key < b.key ? a : b;
    }

    public int getOperationCount() {
        return operationCount;
    }

    public void resetOperationCount() {
        operationCount = 0;
    }
}
