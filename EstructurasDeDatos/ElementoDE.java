package EstructurasDeDatos;

/**
 * Clase que representa un nodo de una lista doblemente enlazada (LDE).
 * En una lista doblemente enlazada cada nodo contiene:
 * 1. El elemento que se almacena en la estructura.
 * 2. Una referencia al nodo siguiente.
 * 3. Una referencia al nodo anterior.
 * Gracias a estas dos referencias, la lista puede recorrerse en
 * ambos sentidos: desde el primero hasta el último o desde el
 * último hasta el primero.
 * @param <T> Tipo genérico del elemento que se almacenará en el nodo.
 */
public class ElementoDE<T> {

    /**
     * Variable que almacena el dato contenido en el nodo.
     * Se define como tipo genérico T para que la lista pueda almacenar
     * cualquier tipo de objeto.
     */
    T elemento;

    /**
     * Referencia al siguiente nodo de la lista.
     * Esta referencia permite avanzar en la lista hacia adelante.
     * Si este nodo es el último de la lista, esta referencia será null.
     */
    ElementoDE<T> siguiente;

    /**
     * Referencia al nodo anterior de la lista.
     * Permite recorrer la lista hacia atrás.
     * Si este nodo es el primero de la lista, esta referencia será null.
     */
    ElementoDE<T> anterior;

    /**
     * Constructor del nodo.
     * Se encarga de crear un nuevo nodo que almacenará el elemento
     * recibido como parámetro.
     * Funcionamiento:
     * - Se guarda el elemento en el atributo elemento.
     * - Se inicializan las referencias siguiente y anterior como null,
     *   ya que el nodo todavía no está enlazado con otros nodos.
     * @param elemento Dato que se almacenará en el nodo.
     */
    ElementoDE(T elemento){
        this.elemento = elemento;
        this.siguiente = null;
        this.anterior = null;
    }
}