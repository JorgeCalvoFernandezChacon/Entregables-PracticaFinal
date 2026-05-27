package EstructurasDeDatos;

/**
 * EstructurasDeDatos.Cola
 * Permite almacenar elementos comparables y realizar operaciones sobre la lista
 * como mater (enqueue), sacar (dequeue) u obtener (peek), o saber el tamaño de la lista.
 * @param <T> Es un parámetro de typo, con la restricción que tiene que ser comparable
 *           para el caso en que queramos hacer listas ordenadas
 * La estructura está formada por nodos enlazados con referencias al siguiente elemento.
 * Es una estructura FIFO, first in first out, por lo que los nodos se van enfilando.
 */
public class Cola<T> implements ContratoCola<T>{
    private ElementoCola<T> primero; // Referencia al primer elemento de la cola
    private ElementoCola<T> ultimo; // Referencia al último elemento de la cola
    private int tamaño = 0; // La cola se inicializa vacía

    @Override
    /**
     * Elimina y devuelve el elemento que se encuentra al frente de la cola.
     * Esta operación reduce en uno el número de elementos de la cola.
     * @return El elemento que estaba al frente de la cola,
     *         o null si la cola está vacía.
     */
    public T dequeue() {
        if(tamaño == 0)return null; // Si la cola está vacía devuelve null
        T elemento = primero.elemento; // Guardamos la información del primer elemento
        primero = primero.siguiente; // El siguiente en la cola pasa a ser el primero
        tamaño --; // Reducimos en uno el tamaño
        if(primero == null){ // Si la cola queda vacía, el último también es vacío
            ultimo = null;
        }
        return elemento;
    }

    @Override
    /**
     * Inserta un nuevo elemento al final de la cola.
     * El elemento insertado será el último en salir hasta que se extraigan
     * todos los elementos anteriores.
     * @param elemento Elemento que se desea añadir a la cola.
     */
    public void enqueue(T elemento) {
        ElementoCola<T> nuevo = new ElementoCola<>(elemento); // Creamos un nuevo elemento de la clase EstructurasDeDatos.ElementoCola
        if(tamaño == 0){ // Si la lista está vacía el nuevo es el primero y el último
            primero = nuevo;
            ultimo = nuevo;
        }
        else{ // Si no está vacía, el nuevo es el siguiente del último, y por tanto el nuevo último
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }
        tamaño ++; // Ampliamos el tamaño en uno

    }

    @Override
    /**
     * Devuelve el elemento que se encuentra al frente de la cola sin eliminarlo.
     * Esta operación permite consultar el primer elemento de la cola
     * manteniendo intacto el contenido de la estructura.
     * @return Elemento situado al frente de la cola,
     *         o null si la cola está vacía.
     */
    public T peek() {
        if(tamaño == 0)return null; // Si la cola es vacía devuelve null
        return primero.elemento; // Si no, devuelve el primer elemento de la cola
    }

    @Override
    /**
     * Comprueba si la cola está vacía.
     * @return true si la cola no contiene elementos,
     *         false en caso contrario.
     */
    public boolean vacia() {
        return (tamaño == 0);
    }

    @Override
    /**
     * Devuelve el número de elementos almacenados actualmente en la cola.
     * @return Tamaño actual de la cola.
     */
    public int size() {
        return tamaño;
    }

    @Override
    /**
     * Devuelve un iterador para recorrer los elementos de la cola.
     * El iterador permite acceder secuencialmente a los elementos
     * almacenados en la estructura sin modificarla.
     *
     * @return Iterador que permite recorrer la cola.
     */
    public ContratoIterador<T> Iterador() {
        return new IteradorCola<>(primero);
    }
}