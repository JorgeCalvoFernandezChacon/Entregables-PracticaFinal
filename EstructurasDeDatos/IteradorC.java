package EstructurasDeDatos;

/**
 * Clase que implementa un iterador para recorrer una lista circular doblemente enlazada.
 *
 * Un iterador permite recorrer los elementos de una estructura de datos
 * sin acceder directamente a su implementación interna. En este caso,
 * el iterador avanza nodo a nodo desde el primer elemento de la cola
 * hasta el último.
 *
 * @param <T> Tipo de dato almacenado en los nodos de la cola.
 */
public class IteradorC<T> implements ContratoIterador<T>{
    /**
     * Referencia al nodo que está siendo apuntado actualmente por el iterador.
     */
    private ElementoC<T> apuntado;
    /**
     * Cuenta los nodos recorridos actualmente por el iterador.
     */
    private int recorridos;
    /**
     * Cuenta el tamaño de la lista.
     */
    private int tamaño;

    public IteradorC(ElementoC<T> comienzo, int tamaño){
        /**
         * Inicializa el iterador indicando desde qué nodo debe comenzar el recorrido y
         * el tamaño de la lista para recorrerla una sola vez.
         * Normalmente este nodo será el primer elemento de la cola.
         *
         * @param comienzo Nodo inicial desde el que el iterador empezará a recorrer la cola.
         * @param tamaño Tamaño de la lista
         */
        this.apuntado = comienzo;
        this.recorridos = 0;
        this.tamaño = tamaño;
    }

    @Override
    /**
     * Comprueba si todavía quedan elementos por recorrer en la cola.
     * false si el iterador ya ha llegado al final de la cola.
     */
    public boolean haySiguiente() {
        return (recorridos < tamaño);
    }

    @Override
    /**
     * Devuelve el siguiente elemento del recorrido y avanza el iterador.
     * Este mecanismo permite recorrer la cola
     * desde el primer nodo hasta el último.
     */
    public T siguiente() {
        T elemento = apuntado.elemento; // Se guarda el elemento del nodo actualmente apuntado
        apuntado = apuntado.siguiente; // El iterador avanza al siguiente nodo de la cola
        recorridos ++; // Se añade uno al contador de nodos recorridos
        return elemento; // Se devuelve el elemento obtenido antes de avanzar
    }
}