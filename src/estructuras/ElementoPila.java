package estructuras;

public class ElementoPila<T> {
    T elemento;
    ElementoPila<T> siguiente;

    ElementoPila(T elemento) {
        this.elemento = elemento;
        this.siguiente = null;
    }
}
