package estructuras;

public class BFS {

    public static ListaEnlazada<Integer> findShortestPath(Grafo grafo, int start, int end) {
        int numVertices = grafo.getNumVertices();
        int[] parent = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            parent[i] = -1;
        }

        boolean[] visited = new boolean[numVertices];
        Cola<Integer> queue = new Cola<>();

        visited[start] = true;
        queue.enqueue(start);

        boolean found = false;
        while (!queue.vacia()) {
            int current = queue.dequeue();
            if (current == end) {
                found = true;
                break;
            }

            ListaEnlazada<Integer> neighbors = grafo.getNeighbors(current);
            for (int i = 0; i < neighbors.size(); i++) {
                int neighbor = neighbors.get(i);
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    queue.enqueue(neighbor);
                }
            }
        }

        if (!found) {
            return null;
        }

        ListaEnlazada<Integer> path = new ListaEnlazada<>();
        int curr = end;
        while (curr != -1) {
            path.add(curr);
            curr = parent[curr];
        }

        return reversePath(path);
    }

    public static ListaEnlazada<Integer> findShortestPathGrid(int[][] grid, int startRow, int startCol, int endRow, int endCol) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] parent = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                parent[i][j] = -1;
            }
        }

        Cola<Integer> queue = new Cola<>();
        int startNode = startRow * cols + startCol;
        queue.enqueue(startNode);
        visited[startRow][startCol] = true;

        boolean found = false;
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        while (!queue.vacia()) {
            int currNode = queue.dequeue();
            int r = currNode / cols;
            int c = currNode % cols;

            if (r == endRow && c == endCol) {
                found = true;
                break;
            }

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 0 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    parent[nr][nc] = currNode;
                    queue.enqueue(nr * cols + nc);
                }
            }
        }

        if (!found) {
            return null;
        }

        ListaEnlazada<Integer> path = new ListaEnlazada<>();
        int currR = endRow;
        int currC = endCol;
        while (true) {
            path.add(currR * cols + currC);
            int pNode = parent[currR][currC];
            if (pNode == -1) {
                break;
            }
            currR = pNode / cols;
            currC = pNode % cols;
        }

        return reversePath(path);
    }

    private static <T> ListaEnlazada<T> reversePath(ListaEnlazada<T> path) {
        ListaEnlazada<T> reversed = new ListaEnlazada<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reversed.add(path.get(i));
        }
        return reversed;
    }
}
