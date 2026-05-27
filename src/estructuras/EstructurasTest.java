package estructuras;

public class EstructurasTest {

    public static void main(String[] args) {
        System.out.println("--- Ejecutando Pruebas Unitarias de Estructuras ---");
        
        testListaEnlazada();
        testCola();
        testGrafo();
        testBFS();
        
        System.out.println("\n--- Pruebas Unitarias Finalizadas ---");
    }

    private static void testListaEnlazada() {
        System.out.println("\n[Test: ListaEnlazada]");
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        
        // Inserciones
        lista.add(10);
        lista.add(20);
        lista.add(30);
        if (lista.size() == 3) {
            System.out.println("Éxito: Tamaño correcto tras inserciones.");
        } else {
            System.out.println("Error: Tamaño incorrecto. Esperado: 3, Obtenido: " + lista.size());
        }

        // Acceso por índice
        if (lista.get(0) == 10 && lista.get(1) == 20 && lista.get(2) == 30) {
            System.out.println("Éxito: Acceso por índice correcto.");
        } else {
            System.out.println("Error: Acceso por índice incorrecto.");
        }

        // Eliminaciones
        if (lista.remove(20)) {
            if (lista.size() == 2 && lista.get(1) == 30) {
                System.out.println("Éxito: Eliminación correcta.");
            } else {
                System.out.println("Error: Estado de la lista incorrecto tras eliminación.");
            }
        } else {
            System.out.println("Error: No se pudo eliminar el elemento.");
        }

        // Tamaño y vacío
        if (!lista.isEmpty() && lista.size() == 2) {
            System.out.println("Éxito: isEmpty y size correctos.");
        } else {
            System.out.println("Error: isEmpty o size incorrectos.");
        }
    }

    private static void testCola() {
        System.out.println("\n[Test: Cola]");
        Cola<Integer> cola = new Cola<>();

        // Enqueue
        cola.enqueue(1);
        cola.enqueue(2);
        cola.enqueue(3);
        if (cola.size() == 3 && cola.peek() == 1) {
            System.out.println("Éxito: Enqueue y peek correctos.");
        } else {
            System.out.println("Error: Enqueue o peek incorrectos.");
        }

        // Dequeue
        if (cola.dequeue() == 1) {
            if (cola.size() == 2 && cola.peek() == 2) {
                System.out.println("Éxito: Dequeue correcto.");
            } else {
                System.out.println("Error: Estado de la cola incorrecto tras dequeue.");
            }
        } else {
            System.out.println("Error: Dequeue devolvió el valor incorrecto.");
        }

        // isEmpty
        cola.dequeue();
        cola.dequeue();
        if (cola.vacia() && cola.size() == 0) {
            System.out.println("Éxito: isEmpty y size correctos al vaciar.");
        } else {
            System.out.println("Error: isEmpty o size incorrectos al vaciar.");
        }
    }

    private static void testGrafo() {
        System.out.println("\n[Test: Grafo]");
        Grafo grafo = new Grafo(4);

        // Añadir aristas
        grafo.addEdge(0, 1, true);
        grafo.addEdge(1, 2, true);
        grafo.addEdge(2, 3, false); // 2 -> 3

        if (grafo.getNumVertices() == 4) {
            System.out.println("Éxito: Número de vértices correcto.");
        } else {
            System.out.println("Error: Número de vértices incorrecto.");
        }

        // Obtener vecinos
        ListaEnlazada<Integer> vecinos0 = grafo.getNeighbors(0);
        if (vecinos0.size() == 1 && vecinos0.get(0) == 1) {
            System.out.println("Éxito: Vecinos del vértice 0 correctos.");
        } else {
            System.out.println("Error: Vecinos del vértice 0 incorrectos.");
        }

        ListaEnlazada<Integer> vecinos1 = grafo.getNeighbors(1);
        if (vecinos1.size() == 2) {
            System.out.println("Éxito: Vecinos del vértice 1 correctos (bidireccional).");
        } else {
            System.out.println("Error: Vecinos del vértice 1 incorrectos.");
        }

        ListaEnlazada<Integer> vecinos3 = grafo.getNeighbors(3);
        if (vecinos3.isEmpty()) {
            System.out.println("Éxito: Vecinos del vértice 3 correctos (unidireccional).");
        } else {
            System.out.println("Error: Vecinos del vértice 3 no deberían existir.");
        }
    }

    private static void testBFS() {
        System.out.println("\n[Test: BFS]");
        
        // Test BFS en Grafo
        Grafo grafo = new Grafo(5);
        grafo.addEdge(0, 1, true);
        grafo.addEdge(0, 2, true);
        grafo.addEdge(1, 3, true);
        grafo.addEdge(2, 3, true);
        grafo.addEdge(3, 4, true);

        ListaEnlazada<Integer> path = BFS.findShortestPath(grafo, 0, 4);
        if (path != null && path.size() == 4) { // 0 -> 1/2 -> 3 -> 4
            System.out.println("Éxito: Camino más corto en grafo encontrado.");
        } else {
            System.out.println("Error: Camino más corto en grafo incorrecto o no encontrado.");
        }

        // Test BFS en Matriz (Grid)
        int[][] grid = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        // Camino de (0,0) a (2,2) evitando (1,1)
        // Camino posible: (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2)
        ListaEnlazada<Integer> pathGrid = BFS.findShortestPathGrid(grid, 0, 0, 2, 2);
        if (pathGrid != null && pathGrid.size() == 5) {
            System.out.println("Éxito: Camino más corto en matriz encontrado.");
        } else {
            System.out.println("Error: Camino más corto en matriz incorrecto o no encontrado.");
        }
    }
}
