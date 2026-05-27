package estructuras;

public interface ContratoPila<T> {
    T pop();
    void push(T elemento);
    T peek();
    boolean vacia();
    int tamanoLista();
    ContratoIterador<T> Iterador();
}
