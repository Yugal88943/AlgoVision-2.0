package graph;

import java.util.*;

public class Graph {
    private int V;
    private LinkedList<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public Graph(int v) {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++)
            adj[i] = new LinkedList<>();
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
    }

    public String bfs(int start) {
        boolean[] visited = new boolean[V];
        Queue<Integer> q = new LinkedList<>();
        StringBuilder path = new StringBuilder();

        visited[start] = true;
        q.add(start);

        while (!q.isEmpty()) {
            int node = q.poll();
            path.append("Area ").append(node).append(" - ");

            for (int n : adj[node]) {
                if (!visited[n]) {
                    visited[n] = true;
                    q.add(n);
                }
            }
        }

        return path.toString();
    }
}