package estructuras;

public class ElementoC<T> {
    T elemento;
    ElementoC<T> siguiente;
    ElementoC<T> anterior;

    ElementoC(T elemento) {
        this.elemento = elemento;
        this.siguiente = null;
        this.anterior = null;
    }
}
