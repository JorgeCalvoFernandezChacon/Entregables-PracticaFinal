package estructuras;

public class Arbol<T> {
    private Nodo<T> root;

    public static class Nodo<T> {
        private T data;
        private ListaEnlazada<Nodo<T>> children;

        public Nodo(T data) {
            this.data = data;
            this.children = new ListaEnlazada<>();
        }

        public T getData() { return data; }
        public ListaEnlazada<Nodo<T>> getChildren() { return children; }

        public Nodo<T> addChild(T childData) {
            Nodo<T> child = new Nodo<>(childData);
            children.add(child);
            return child;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }
    }

    public Arbol() {
        this.root = null;
    }

    public Arbol(T rootData) {
        this.root = new Nodo<>(rootData);
    }

    public Nodo<T> getRoot() { return root; }

    public void setRoot(T rootData) {
        this.root = new Nodo<>(rootData);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public ListaEnlazada<T> preorden() {
        ListaEnlazada<T> result = new ListaEnlazada<>();
        preorden(root, result);
        return result;
    }

    private void preorden(Nodo<T> node, ListaEnlazada<T> result) {
        if (node == null) return;
        result.add(node.data);
        for (int i = 0; i < node.children.size(); i++) {
            preorden(node.children.get(i), result);
        }
    }

    public int altura() {
        return altura(root);
    }

    private int altura(Nodo<T> node) {
        if (node == null) return 0;
        int max = 0;
        for (int i = 0; i < node.children.size(); i++) {
            int h = altura(node.children.get(i));
            if (h > max) max = h;
        }
        return max + 1;
    }
}
