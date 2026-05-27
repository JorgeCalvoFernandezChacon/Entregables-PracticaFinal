package EstructurasDeDatos;

/**
 * Interfaz que define el contrato de funcionamiento de una pila.
 * Esta interfaz especifica las operaciones básicas que cualquier
 * implementación de pila debe proporcionar, como insertar elementos,
 * eliminar el elemento de la cima o consultar el estado de la estructura.
 * @param <T> Tipo de elemento almacenado en la pila. Se restringe a
 *            tipos comparables para permitir comparaciones entre elementos
 *            si la implementación lo requiere.
 */
public interface ContratoPila<T extends Comparable<T>> {
    /**
     * Elimina y devuelve el elemento que se encuentra en la cima de la pila.
     * Esta operación reduce en uno el número de elementos de la pila.
     * @return El elemento que estaba en la cima de la pila,
     *         o null si la pila está vacía.
     */
    T pop();

    /**
     * Inserta un nuevo elemento en la cima de la pila.
     * El elemento insertado pasa a ser el primero que se devolverá
     * en la siguiente operación pop().
     * @param elemento Elemento que se desea introducir en la pila.
     */
    void push(T elemento);

    /**
     * Devuelve el elemento que se encuentra en la cima de la pila
     * sin eliminarlo de la estructura.
     * Esta operación permite consultar el último elemento insertado
     * manteniendo intacto el contenido de la pila.
     * @return Elemento situado en la cima de la pila,
     *         o null si la pila está vacía.
     */
    T peek();

    /**
     * Comprueba si la pila está vacía.
     * @return true si la pila no contiene elementos,
     *         false en caso contrario.
     */
    boolean vacia();

    /**
     * Devuelve el número de elementos almacenados actualmente en la pila.
     * @return Tamaño actual de la pila.
     */
    int tamañoLista();

    /**
     * Devuelve un iterador para recorrer los elementos de la pila.
     * El iterador permite acceder secuencialmente a los elementos
     * almacenados en la estructura sin modificarla.
     * @return Iterador que permite recorrer la pila.
     */
    ContratoIterador<T> Iterador();
}
