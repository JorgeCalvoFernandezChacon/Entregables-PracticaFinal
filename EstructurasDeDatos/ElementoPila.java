package EstructurasDeDatos;

/**
 * Clase que representa un nodo de una lista pila.
 * Una lista pila está formada por nodos conectados entre sí
 * mediante referencias al siguiente nodo de la estructura. Cada nodo almacena
 * dos cosas fundamentales:
 * 1. El dato o elemento que se quiere guardar en la lista.
 * 2. Una referencia al siguiente nodo de la lista.
 * @param <T> Tipo genérico del elemento que se almacenará en el nodo.
 *            Esto permite que la lista pueda almacenar cualquier tipo
 *            de objeto.
 */
public class ElementoPila<T>{
    /**
     * Variable que almacena el dato contenido en el nodo.
     * Se define como tipo genérico T para permitir que la lista
     * sea reutilizable con cualquier tipo de objeto.
     */
    T elemento;

    /**
     * Referencia al siguiente nodo de la lista.
     * Esta variable permite enlazar un nodo con el siguiente formando
     * la cadena de nodos.
     * Si este nodo es el último de la lista, esta referencia será null,
     * indicando que no existe un nodo siguiente.
     */
    ElementoPila<T> siguiente;

    /**
     * Constructor de la clase nodo.
     * Se encarga de crear un nuevo nodo que almacenará el elemento
     * recibido como parámetro.
     * Funcionamiento:
     * - Se asigna el valor del parámetro al campo elemento.
     * - Se inicializa la referencia al siguiente nodo como null,
     *   ya que en el momento de crear el nodo todavía no está
     *   enlazado con ningún otro.
     * @param elemento Dato que se almacenará en el nodo.
     */
    ElementoPila(T elemento){
        this.elemento = elemento;
        this.siguiente = null;
    }
}