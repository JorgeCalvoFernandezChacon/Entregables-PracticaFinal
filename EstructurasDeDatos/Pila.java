package EstructurasDeDatos;

/**
 * EstructurasDeDatos.Pila
 * Permite almacenar elementos comparables y realizar operaciones sobre la lista
 * como meter (push), sacar (pop) u obtener (peek), o saber el tamaño de la lista.
 * @param <T> Es un parámetro de typo, con la restricción que tiene que ser comparable
 *           para el caso en que queramos hacer listas ordenadas
 * La estructura está formada por nodos enlazados con referencias al siguiente elemento.
 * Es una estructura LIFO, last in first out, por lo que los nodos se van apilando.
 */
public class Pila<T extends Comparable<T>> implements ContratoPila<T>{
    private ElementoPila<T> cima; // Referencia al primer elemento de la pila
    private int tamaño = 0; // La pila se inicializa sin elementos
    @Override
    /**
     * Se saca el primer elemento de la pila, la cima,
     * es decir, el que ha entrado último.
     *
     */
    public T pop() {
        if(tamaño == 0)return null; // Si la lista está vacía no se puede sacar ninguno
        T elemento = cima.elemento; // Guardamos la información del elemento de la cima
        cima = cima.siguiente; // La cima pasa a ser el siguiente elemento
        tamaño --; // Reducimos en uno el tamaño de la pila
        return elemento; // Devolvemos la información del elemento de la cima
    }

    @Override
    /**
     * Se añade el elemento marcado a la cima de la pila
     * @param elemento Elemento que se desea introducir en la pila.
     */
    public void push(T elemento) {
        ElementoPila<T> nuevo = new ElementoPila<>(elemento); // Creamos un nuevo elemento de clase EstructurasDeDatos.ElementoPila
        nuevo.siguiente = cima; // Asignamos al nuevo elemento la referencia siguiente de la cima
        cima = nuevo; // El nuevo pasa a ser la cima de la pila
        tamaño ++; // Aumentamos el tamaño de la pila en uno
    }

    @Override
    /**
     * Devuelve el elemento en la cima de la pila
     * @return Elemento situado en la cima de la pila,
     *         o null si la pila está vacía.
     */
    public T peek() {
        if(tamaño == 0)return null; // Si la lista está vacía devuelve null
        return cima.elemento; // Si no, devuelve el elemento de la cima de la pila
    }

    @Override
    /** Devuelve si la pila está vacía
     * @return true si la pila no contiene elementos,
     *         false en caso contrario.
     */
    public boolean vacia() {
        return (tamaño == 0);
    }

    @Override
    /**
     * Devuelve el número de elementos almacenados actualmente en la pila.
     * @return Tamaño actual de la pila.
     */
    public int tamañoLista() {
        return tamaño;
    }

    @Override
    /**
     * Devuelve un iterador para recorrer los elementos de la pila.
     * El iterador permite acceder secuencialmente a los elementos
     * almacenados en la estructura sin modificarla.
     * @return Iterador que permite recorrer la pila.
     */
    public ContratoIterador<T> Iterador() {
        return new IteradorPila<>(cima);
    }
}