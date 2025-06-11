import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.IntStream;

public class GenerataRandomNumbers  {
    private static final String OUTPUT_DIR = "datasets";
    private static final int MIN_SIZE = 100;
    private static final int MAX_SIZE = 10_000;
    private static final int NUM_DATASETS = 50;
    private static final int MIN_VALUE = -1000;
    private static final int MAX_VALUE = 1000;

    public static void main(String[] args) throws IOException {
        createOutputDirectory();
        generateDatasets();
    }

    private static void createOutputDirectory() throws IOException {
        Path path = Paths.get(OUTPUT_DIR);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    private static void generateDatasets() {
        Random random = new Random();
        IntStream.rangeClosed(1, NUM_DATASETS)
                .parallel()
                .forEach(i -> {
                    int size = calculateDatasetSize(i);
                    String filename = String.format("%s/dataset_%04d_%d.txt", 
                                                  OUTPUT_DIR, size, i);
                    try {
                        generateDatasetFile(filename, size, random);
                        System.out.printf("Created: %s\n", filename);
                    } catch (IOException e) {
                        System.err.printf("Error creating %s: %s\n", filename, e.getMessage());
                    }
                });
    }

    private static int calculateDatasetSize(int index) {
        // Экспоненциальное распределение размеров
        double fraction = Math.pow((double) index / NUM_DATASETS, 2);
        return MIN_SIZE + (int) (fraction * (MAX_SIZE - MIN_SIZE));
    }

    private static void generateDatasetFile(String filename, int size, Random random) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int value = MIN_VALUE + random.nextInt(MAX_VALUE - MIN_VALUE + 1);
            sb.append(value).append(i < size - 1 ? " " : "");
        }
        Files.writeString(Path.of(filename), sb.toString());
    }
}
