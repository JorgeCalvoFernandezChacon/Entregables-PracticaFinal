package EstructurasDeDatos;

/**
 * Clase que representa un nodo de una lista circular doblemente enlazada.
 * En este tipo de lista cada nodo contiene:
 * 1. El elemento que se quiere almacenar.
 * 2. Una referencia al siguiente nodo de la lista.
 * 3. Una referencia al nodo anterior.
 * La diferencia con una lista doblemente enlazada normal es que la
 * estructura es circular, es decir:
 * - El último nodo apunta al primero.
 * - El primer nodo apunta al último.
 * Gracias a esto, la lista puede recorrerse indefinidamente en
 * ambos sentidos sin encontrar referencias null (excepto en el
 * momento de crear los nodos antes de enlazarlos).
 * @param <T> Tipo genérico del elemento almacenado en el nodo.
 */
public class ElementoC<T>{

    /**
     * Variable que almacena el dato contenido en el nodo.
     * Se define como tipo genérico T para que la estructura
     * pueda almacenar cualquier tipo de objeto.
     */
    T elemento;

    /**
     * Referencia al siguiente nodo de la lista circular.
     * Permite avanzar hacia delante en la estructura.
     * En una lista circular, el último nodo tendrá su
     * referencia siguiente apuntando al primer nodo.
     */
    ElementoC<T> siguiente;

    /**
     * Referencia al nodo anterior de la lista circular.
     * Permite recorrer la lista hacia atrás.
     * En una lista circular, el primer nodo tendrá su
     * referencia anterior apuntando al último nodo.
     */
    ElementoC<T> anterior;

    /**
     * Constructor del nodo.
     * Se encarga de crear un nuevo nodo que almacenará
     * el elemento recibido como parámetro.
     * Inicialmente las referencias siguiente y anterior
     * se establecen en null porque el nodo todavía no
     * está enlazado con otros nodos de la lista.
     * @param elemento Dato que se almacenará en el nodo.
     */
    ElementoC(T elemento){
        this.elemento = elemento;
        this.siguiente = null;
        this.anterior = null;
    }
}