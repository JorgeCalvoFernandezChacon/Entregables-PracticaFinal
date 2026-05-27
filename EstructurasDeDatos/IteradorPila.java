package EstructurasDeDatos;

/**
 * Clase que implementa un iterador para recorrer una pila enlazada.
 *
 * Un iterador permite recorrer los elementos de una estructura de datos
 * sin acceder directamente a su implementación interna. En este caso,
 * el iterador avanza nodo a nodo desde el primer elemento de la cola
 * hasta el último.
 *
 * @param <T> Tipo de dato almacenado en los nodos de la cola.
 */
public class IteradorPila<T> implements ContratoIterador{
    /**
     * Referencia al nodo que está siendo apuntado actualmente por el iterador.
     */
    private ElementoPila<T> apuntado;
    /**
     * Inicializa el iterador indicando desde qué nodo debe comenzar el recorrido.
     * Normalmente este nodo será el primer elemento de la cola.
     *
     * @param comienzo Nodo inicial desde el que el iterador empezará a recorrer la cola.
     */
    public IteradorPila(ElementoPila<T> cima){
        this.apuntado = cima;
    }

    @Override
    /**
     * Comprueba si todavía quedan elementos por recorrer en la cola.
     * false si el iterador ya ha llegado al final de la cola.
     */
    public boolean haySiguiente() {
        return (apuntado != null);
    }

    @Override
    /**
     * Devuelve el siguiente elemento del recorrido y avanza el iterador.
     * Este mecanismo permite recorrer la cola
     * desde el primer nodo hasta el último.
     */
    public T siguiente() {
        if(haySiguiente()==false) {
            return null;
        } // Si no hay más elementos en la cola, se devuelve null
        T elemento = apuntado.elemento; // Se guarda el elemento del nodo actualmente apuntado
        apuntado = apuntado.siguiente; // El iterador avanza al siguiente nodo de la cola
        return elemento; // Se devuelve el elemento obtenido antes de avanzar
    }
}