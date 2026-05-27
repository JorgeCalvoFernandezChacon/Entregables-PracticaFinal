package EstructurasDeDatos;

/**
 * Interfaz que define el contrato de funcionamiento de una lista genérica.
 * Esta interfaz especifica las operaciones básicas que cualquier implementación
 * de lista debe proporcionar, incluyendo inserción, eliminación, búsqueda
 * y consulta de estado.
 * Al usar un parámetro genérico T que extiende Comparable<T>, se establece
 * que los elementos de la lista pueden compararse entre sí, lo cual es
 * útil para mantener orden, realizar búsquedas o comparaciones, sobre todo para listas
 * con ordenadas.
 * @param <T> Tipo de elemento almacenado en la lista. Debe ser comparable.
 */
public interface ContratoListas<T> {

    /**
     * Elimina un elemento de la lista.
     * @param elemento Elemento que se desea eliminar.
     * @return El elemento eliminado si existía en la lista,
     *         o null si no se encontraba.
     */
    T eliminar(T elemento);

    /**
     * Obtiene un elemento de la lista.
     * @param elemento Elemento que se desea buscar.
     * @return El elemento encontrado, o null si no está en la lista.
     */
    T obtener(T elemento);

    /**
     * Añade un elemento a la lista.
     * @param elemento Elemento que se desea insertar.
     */
    void añadir(T elemento);

    /**
     * Comprueba si la lista está vacía.
     * @return true si no contiene elementos, false en caso contrario.
     */
    boolean vacio();

    /**
     * Devuelve el número de elementos que hay actualmente en la lista.
     * @return Tamaño de la lista.
     */
    int tamañoLista();

    /**
     * Devuelve un iterador para recorrer la lista.
     * Permite acceder secuencialmente a los elementos sin exponer
     * la implementación interna de la lista.
     * @return Iterador que permite recorrer la lista.
     */
    ContratoIterador<T> Iterador();
}