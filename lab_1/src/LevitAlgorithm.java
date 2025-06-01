import java.util.*;

public class LevitAlgorithm {
    private List<List<Edge>> graph;
    private int n;
    private long iterations;

    private static class Edge {
        int to, weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public LevitAlgorithm(int n) {
        this.n = n;
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        graph.get(from).add(new Edge(to, weight));
    }

    public int[] levit(int start) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;

        int[] state = new int[n];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(start);
        state[start] = 1;

        iterations = 0;

        while (!queue.isEmpty()) {
            int v = queue.pollFirst();
            state[v] = 2;

            for (Edge edge : graph.get(v)) {
                int u = edge.to;
                int w = edge.weight;
                iterations++;

                if (dist[u] > dist[v] + w) {
                    dist[u] = dist[v] + w;
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

    public long getIterations() {
        return iterations;
    }
}
