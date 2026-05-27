package EstructurasDeDatos;

/**
 * Clase que implementa un iterador para recorrer una cola enlazada.
 *
 * Un iterador permite recorrer los elementos de una estructura de datos
 * sin acceder directamente a su implementación interna. En este caso,
 * el iterador avanza nodo a nodo desde el primer elemento de la cola
 * hasta el último.
 *
 * @param <T> Tipo de dato almacenado en los nodos de la cola.
 */
public class IteradorCola<T> implements ContratoIterador<T>{

    /**
     * Referencia al nodo que está siendo apuntado actualmente por el iterador.
     */
    private ElementoCola<T> apuntado;

    /**
     * Inicializa el iterador indicando desde qué nodo debe comenzar el recorrido.
     * Normalmente este nodo será el primer elemento de la cola.
     *
     * @param comienzo Nodo inicial desde el que el iterador empezará a recorrer la cola.
     */
    public IteradorCola(ElementoCola<T> comienzo){
        this.apuntado = comienzo;
    }

    /**
     * Comprueba si todavía quedan elementos por recorrer en la cola.
     * false si el iterador ya ha llegado al final de la cola.
     */
    @Override
    public boolean haySiguiente() {
        return (apuntado != null);
    }

    /**
     * Devuelve el siguiente elemento del recorrido y avanza el iterador.
     * Este mecanismo permite recorrer la cola
     * desde el primer nodo hasta el último.
     */
    @Override
    public T siguiente() {
        if(haySiguiente()==false) { // Si no hay más elementos en la cola, se devuelve null
            return null;
        }
        T elemento = apuntado.elemento; // Se guarda el elemento del nodo actualmente apuntado
        apuntado = apuntado.siguiente;
        return elemento;
    }
}