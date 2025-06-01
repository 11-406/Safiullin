import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class CreateGraph {
    public static void generateGraph(String filename, int minVertices, int maxVertices) {
        Random random = new Random();
        int n = random.nextInt(maxVertices - minVertices + 1) + minVertices;
        int m = n * 2;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(n + " " + m);
            for (int i = 0; i < m; i++) {
                int from = random.nextInt(n);
                int to = random.nextInt(n);
                int weight = random.nextInt(20) + 1;
                writer.println(from + " " + to + " " + weight);
            }
            System.out.println("Граф создан и записан в " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

