import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numVertices = getRandomNumber(100, 10000); // Случайное количество вершин от 100 до 10 000
        int numEdges = numVertices * 2; // Количество рёбер в 2 раза больше вершин

        String fileName = "graph.txt";

        // Генерация графа и сохранение в файл
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println(numVertices + " " + numEdges);
            Random rand = new Random();

            for (int i = 0; i < numEdges; i++) {
                int u = rand.nextInt(numVertices);
                int v = rand.nextInt(numVertices);
                int weight = rand.nextInt(20) + 1; // веса от 1 до 20
                writer.println(u + " " + v + " " + weight);
            }

            System.out.println("Граф сгенерирован и сохранён в файл: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Загрузка графа из файла
        Graph graph = null;
        try (Scanner scanner = new Scanner(new File(fileName))) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            graph = new Graph(n);

            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                int w = scanner.nextInt();
                graph.addEdge(u, v, w);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Запуск алгоритма Левита
        long startTime = System.nanoTime();
        int[] distances = graph.levit(0); // Начинаем с вершины 0
        long endTime = System.nanoTime();

        // Вывод результатов
        System.out.println("\nМинимальные расстояния от вершины 0 до первых 10 вершин:");
        for (int i = 0; i < Math.min(distances.length, 10); i++) {
            System.out.println("До вершины " + i + ": " + distances[i]);
        }

        System.out.println("\nКоличество итераций: " + graph.getIterations());
        System.out.println("Время выполнения (миллисекунды): " + (endTime - startTime) / 1_000_000.0);
    }

    // Генератор случайного числа от min до max включительно
    private static int getRandomNumber(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}

// Класс графа
class Graph {
    private int n; // Количество вершин
    private List<List<Edge>> adj; // Список смежности
    private long iterations; // Количество итераций

    public Graph(int n) {
        this.n = n;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Класс ребра
    static class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    // Добавление ребра
    public void addEdge(int from, int to, int weight) {
        adj.get(from).add(new Edge(to, weight));
    }

    // Алгоритм Левита
    public int[] levit(int start) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(start);

        int[] state = new int[n]; // 0 - M0, 1 - M1, 2 - M2
        state[start] = 1;

        iterations = 0; // Обнуление счётчика

        while (!queue.isEmpty()) {
            int v = queue.pollFirst();
            state[v] = 2; // Переводим в множество M2

            for (Edge edge : adj.get(v)) {
                int u = edge.to;
                int weight = edge.weight;
                iterations++;

                if (dist[u] > dist[v] + weight) {
                    dist[u] = dist[v] + weight;

                    if (state[u] == 0) {
                        queue.addLast(u);
                        state[u] = 1;
                    } else if (state[u] == 2) {
                        queue.addFirst(u);
                        state[u] = 1;
                    }
                }
            }
        }

        return dist;
    }

    // Получить количество итераций
    public long getIterations() {
        return iterations;
    }
}
