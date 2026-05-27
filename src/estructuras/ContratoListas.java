package estructuras;

public interface ContratoListas<T> {
    T eliminar(T elemento);
    T obtener(T elemento);
    void añadir(T elemento);
    boolean vacio();
    int tamañoLista();
    ContratoIterador<T> Iterador();
}
