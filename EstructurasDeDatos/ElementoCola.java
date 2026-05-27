package EstructurasDeDatos;

/**
 * Clase que representa un nodo de una cola enlazada.
 * Una cola es una estructura de datos lineal que sigue el principio
 * FIFO (First In, First Out), es decir, el primer elemento que entra
 * en la cola es el primero que sale.
 * La cola se implementa mediante nodos enlazados. Cada nodo contiene:
 * 1. El elemento que se almacena en la cola.
 * 2. Una referencia al siguiente nodo de la cola.
 * @param <T> Tipo genérico del elemento almacenado en el nodo.
 */
public class ElementoCola<T>{

    /**
     * Variable que almacena el dato contenido en el nodo.
     * Se define como tipo genérico T para que la cola pueda almacenar
     * cualquier tipo de objeto.
     */
    T elemento;

    /**
     * Referencia al siguiente nodo de la cola.
     * Esta referencia permite enlazar un nodo con el siguiente
     * formando la estructura de la cola.
     * Si este nodo es el último de la cola, esta referencia será null,
     * indicando que no existe un nodo posterior.
     */
    ElementoCola<T> siguiente;

    /**
     * Constructor del nodo.
     * Se encarga de crear un nuevo nodo que almacenará el elemento
     * recibido como parámetro.
     * Funcionamiento:
     * - Se asigna el valor del parámetro al atributo elemento.
     * - Se inicializa la referencia siguiente a null, ya que
     *   inicialmente el nodo no está enlazado con ningún otro nodo.
     * @param elemento Dato que se almacenará en el nodo de la cola.
     */
    ElementoCola(T elemento){
        this.elemento = elemento;
        this.siguiente = null;
    }
}