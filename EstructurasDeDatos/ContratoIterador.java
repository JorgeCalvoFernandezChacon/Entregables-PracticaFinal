package EstructurasDeDatos;

/**
 * Interfaz que define el contrato de un iterador para recorrer
 * estructuras de datos genéricas.
 * Un iterador permite recorrer los elementos de una estructura
 * sin exponer directamente la implementación interna.
 * @param <T> Tipo de elemento que será recorrido por el iterador.
 */
public interface ContratoIterador<T> {

    /**
     * Comprueba si el iterador todavía tiene elementos por recorrer.
     * Esta operación devuelve true mientras haya al menos un elemento
     * más que se pueda devolver mediante siguiente().
     * @return true si existe al menos un elemento más que devolver,
     *         false si se ha llegado al final de la estructura.
     */
    boolean haySiguiente();

    /**
     * Devuelve el siguiente elemento del recorrido y avanza la posición
     * del iterador.
     * La primera llamada devuelve el primer elemento, la segunda el segundo, y así sucesivamente.
     * @return El elemento actual apuntado por el iterador, o null si no
     *         hay más elementos que recorrer (dependiendo de la implementación).
     */
    T siguiente();
}