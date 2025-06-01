import java.io.*;
import java.util.*;

public class Main {

    private static final int MIN_VERTICES = 100;
    private static final int MAX_VERTICES = 10000;
    private static final int EDGES_PER_VERTEX = 3;
    private static final int TEST_CASES = 50;
    private static final int MIN_WEIGHT = 1;
    private static final int MAX_WEIGHT = 20;

    public static void main(String[] args) {
        generateAndTestGraphs();
    }

    public static void generateAndTestGraphs() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("results.csv"))) {
            writer.println("GraphSize,Time(ns),Iterations");
            
            Random random = new Random();
            for (int i = 0; i < TEST_CASES; i++) {
                
                int vertices = MIN_VERTICES + random.nextInt(MAX_VERTICES - MIN_VERTICES + 1);
                int edges = vertices * EDGES_PER_VERTEX;
                LevitAlgorithm algorithm = new LevitAlgorithm(vertices);

                
                for (int v = 1; v < vertices; v++) {
                    int weight = MIN_WEIGHT + random.nextInt(MAX_WEIGHT - MIN_WEIGHT + 1);
                    algorithm.addEdge(v-1, v, weight);
                }

                
                for (int e = vertices-1; e < edges; e++) {
                    int from = random.nextInt(vertices);
                    int to = random.nextInt(vertices);
                    int weight = MIN_WEIGHT + random.nextInt(MAX_WEIGHT - MIN_WEIGHT + 1);
                    algorithm.addEdge(from, to, weight);
                }

                
                long startTime = System.nanoTime();
                int[] distances = algorithm.levit(0);
                long endTime = System.nanoTime();
                long durationNs = endTime - startTime;
                
                
                writer.printf("%d,%d,%d%n", vertices, durationNs, algorithm.getIterations());
                
                
                System.out.printf("Граф: %d вершин, %d рёбер. Время: %d нс, Итераций: %d%n",
                        vertices, edges, durationNs, algorithm.getIterations());
            }
        } catch (IOException e) {
            System.err.println("Ошибка записи результатов: " + e.getMessage());
        }
    }

    
    private static int[] generateUniqueSizes(int count, int min, int max) {
        return new Random().ints(count, min, max + 1)
                          .distinct()
                          .toArray();
    }
}