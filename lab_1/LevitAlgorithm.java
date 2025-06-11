import java.util.*;

public class LevitAlgorithm {
    private static final int INF = Integer.MAX_VALUE;

    public static int[] findShortestPaths(List<List<Edge>> graph, int start) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        // 0 - не обработана, 1 - в очереди Q1, 2 - в очереди Q2
        int[] state = new int[n];
        Deque<Integer> Q1 = new LinkedList<>();
        Deque<Integer> Q2 = new LinkedList<>();

        Q1.add(start);
        state[start] = 1;

        while (!Q1.isEmpty() || !Q2.isEmpty()) {
            int u;
            if (Q1.isEmpty()) {
                u = Q2.poll();
            } else {
                u = Q1.poll();
            }

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int newDist = dist[u] + edge.weight;

                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    if (state[v] == 0) {
                        Q1.add(v);
                        state[v] = 1;
                    } else if (state[v] == 2) {
                        Q2.remove(v);
                        Q1.add(v);
                        state[v] = 1;
                    }
                }
            }
            state[u] = -1; // Вершина обработана
        }

        return dist;
    }

    static class Edge {
        int to;
        int weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}