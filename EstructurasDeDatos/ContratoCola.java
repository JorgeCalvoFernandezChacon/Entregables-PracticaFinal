package EstructurasDeDatos;

/**
 * Interfaz que define el contrato de funcionamiento de una cola.
 * Esta interfaz especifica las operaciones básicas que cualquier
 * implementación de cola debe proporcionar, como insertar elementos,
 * eliminar el primer elemento, consultar la cabeza o verificar el estado
 * de la estructura.
 * @param <T> Tipo de elemento almacenado en la cola. Se restringe a
 *            tipos comparables para permitir comparaciones entre elementos
 *            si la implementación lo requiere.
 */
public interface ContratoCola<T> {

    /**
     * Elimina y devuelve el elemento que se encuentra al frente de la cola.
     * Esta operación reduce en uno el número de elementos de la cola.
     * @return El elemento que estaba al frente de la cola,
     *         o null si la cola está vacía.
     */
    T dequeue();

    /**
     * Inserta un nuevo elemento al final de la cola.
     * El elemento insertado será el último en salir hasta que se extraigan
     * todos los elementos anteriores.
     * @param elemento Elemento que se desea añadir a la cola.
     */
    void enqueue(T elemento);

    /**
     * Devuelve el elemento que se encuentra al frente de la cola sin eliminarlo.
     * Esta operación permite consultar el primer elemento de la cola
     * manteniendo intacto el contenido de la estructura.
     * @return Elemento situado al frente de la cola,
     *         o null si la cola está vacía.
     */
    T peek();

    /**
     * Comprueba si la cola está vacía.
     * @return true si la cola no contiene elementos,
     *         false en caso contrario.
     */
    boolean vacia();

    /**
     * Devuelve el número de elementos almacenados actualmente en la cola.
     * @return Tamaño actual de la cola.
     */
    int size();

    /**
     * Devuelve un iterador para recorrer los elementos de la cola.
     * El iterador permite acceder secuencialmente a los elementos
     * almacenados en la estructura sin modificarla.
     *
     * @return Iterador que permite recorrer la cola.
     */
    ContratoIterador<T> Iterador();
}