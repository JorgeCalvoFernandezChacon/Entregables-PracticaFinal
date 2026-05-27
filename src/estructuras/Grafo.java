package estructuras;

public class Grafo {
    private int numVertices;
    private ListaEnlazada<Integer>[] adj;

    @SuppressWarnings("unchecked")
    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        this.adj = new ListaEnlazada[numVertices];
        for (int i = 0; i < numVertices; i++) {
            adj[i] = new ListaEnlazada<>();
        }
    }

    public void addEdge(int source, int destination, boolean bidirectional) {
        if (source < 0 || source >= numVertices || destination < 0 || destination >= numVertices) {
            throw new RuntimeException("Vertex index out of bounds");
        }
        adj[source].add(destination);
        if (bidirectional) {
            adj[destination].add(source);
        }
    }

    public ListaEnlazada<Integer> getNeighbors(int vertex) {
        if (vertex < 0 || vertex >= numVertices) {
            throw new RuntimeException("Vertex index out of bounds");
        }
        return adj[vertex];
    }

    public int getNumVertices() {
        return numVertices;
    }

    public ListaEnlazada<Integer>[] getAdjList() {
        return adj;
    }

    public void addEdge(int source, int destination) {
        addEdge(source, destination, true);
    }
}
