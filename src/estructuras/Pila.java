package estructuras;

public class Pila<T> implements ContratoPila<T> {
    private ElementoPila<T> cima;
    private int tamano = 0;

    @Override
    public T pop() {
        if (tamano == 0) return null;
        T elemento = cima.elemento;
        cima = cima.siguiente;
        tamano--;
        return elemento;
    }

    @Override
    public void push(T elemento) {
        ElementoPila<T> nuevo = new ElementoPila<>(elemento);
        nuevo.siguiente = cima;
        cima = nuevo;
        tamano++;
    }

    @Override
    public T peek() {
        if (tamano == 0) return null;
        return cima.elemento;
    }

    @Override
    public boolean vacia() {
        return (tamano == 0);
    }

    @Override
    public int tamanoLista() {
        return tamano;
    }

    @Override
    public ContratoIterador<T> Iterador() {
        return new IteradorPila<>(cima);
    }

    // Wrappers
    public boolean isEmpty() { return vacia(); }
    public int size() { return tamanoLista(); }
}
