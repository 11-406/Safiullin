import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    // Метод для сохранения данных в CSV
    public static void saveToCSV(String fileName, List<Long> times, List<Integer> ops) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Время(нс),Операции\n");
            for (int i = 0; i < times.size(); i++) {
                writer.write(String.format(Locale.US, "%d,%d\n", times.get(i), ops.get(i)));
            }
            System.out.println(" CSV сохранён: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        Random rand = new Random();

        int[] data = new int[10_000];
        for (int i = 0; i < data.length; i++) {
            data[i] = rand.nextInt(1_000_000);
        }

        // Списки для замеров
        List<Long> insertTimes = new ArrayList<>();
        List<Integer> insertOps = new ArrayList<>();
        List<Long> searchTimes = new ArrayList<>();
        List<Integer> searchOps = new ArrayList<>();
        List<Long> deleteTimes = new ArrayList<>();
        List<Integer> deleteOps = new ArrayList<>();

        //  Вставка 10 000 элементов
        for (int value : data) {
            heap.resetOperationCount();
            long start = System.nanoTime();
            heap.insert(value);
            long end = System.nanoTime();
            insertTimes.add(end - start);
            insertOps.add(heap.getOperationCount());
        }

        //  Поиск 100 случайных элементов
        for (int i = 0; i < 100; i++) {
            int value = data[rand.nextInt(data.length)];
            heap.resetOperationCount();
            long start = System.nanoTime();
            heap.contains(value);
            long end = System.nanoTime();
            searchTimes.add(end - start);
            searchOps.add(heap.getOperationCount());
        }

        //  Удаление 1000 случайных элементов
        for (int i = 0; i < 1000; i++) {
            int value = data[rand.nextInt(data.length)];
            heap.resetOperationCount();
            long start = System.nanoTime();
            heap.delete(value);
            long end = System.nanoTime();
            deleteTimes.add(end - start);
            deleteOps.add(heap.getOperationCount());
        }

        //  Подсчёт средних значений
        double avgInsertTime = insertTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double avgInsertOps = insertOps.stream().mapToInt(Integer::intValue).average().orElse(0);

        double avgSearchTime = searchTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double avgSearchOps = searchOps.stream().mapToInt(Integer::intValue).average().orElse(0);

        double avgDeleteTime = deleteTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double avgDeleteOps = deleteOps.stream().mapToInt(Integer::intValue).average().orElse(0);

        //  Консольный вывод на русском
        System.out.printf("ВСТАВКА:\nСреднее время: %.0f наносекунд\nСреднее количество операций: %.2f\n\n",
                avgInsertTime, avgInsertOps);

        System.out.printf("ПОИСК:\nСреднее время: %.0f наносекунд\nСреднее количество операций: %.2f\n\n",
                avgSearchTime, avgSearchOps);

        System.out.printf("УДАЛЕНИЕ:\nСреднее время: %.0f наносекунд\nСреднее количество операций: %.2f\n\n",
                avgDeleteTime, avgDeleteOps);

        //  Сохранение в CSV
        saveToCSV("insert.csv", insertTimes, insertOps);
        saveToCSV("search.csv", searchTimes, searchOps);
        saveToCSV("delete.csv", deleteTimes, deleteOps);
    }
}
