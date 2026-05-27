package EstructurasDeDatos;

/**
 * Lista simplemente enlazada (LSE)
 * Permite almacenar elementos comparables y realizar operaciones sobre la lista
 * como añadir, eliminar u obtener elementos, o saber el tamaño de la lista.
 * @param <T> Es un parámetro de typo, con la restricción que tiene que ser comparable
 *           para el caso en que queramos hacer listas ordenadas
 * La estructura está formada por nodos enlazados con referencias al siguiente elemento.
 */
public class ListaSE<T> {
    protected ElementoSE<T> cabeza; // Referencia al primer elemento de la lista, protected para que las subclases (LSEO) puedan acceder a él
    protected int tamaño = 0; // En un principio la lista es vacía

    /**
     * Elimina un elemento de la lista.
     * @param buscado Elemento que se desea eliminar.
     * @return El elemento eliminado si existía en la lista,
     *         o null si no se encontraba.
     */
    public T del(T buscado) {
        ElementoSE<T> apuntado = cabeza; // Puntero que recorre la lista
        ElementoSE<T> anterior = null; // Elemento anterior del puntero
        while (apuntado!=null){ // Buscamos hasta que el último elemento sea null, el final.
            if(apuntado.elemento.equals(buscado)){
                if(anterior == null){ // Si es el primero elemento, el primero pasa a ser el siguiente
                    cabeza = apuntado.siguiente;
                }
                else anterior.siguiente = apuntado.siguiente; // Si no, se borra asignando al nodo anterior la referencia del siguiente
                tamaño --;
                return apuntado.elemento;
            }
            anterior = apuntado;
            apuntado = apuntado.siguiente;

        }
        return null;
    }

    /**
     * Obtiene un elemento de la lista.
     * @param buscado Elemento que se desea buscar.
     * @return El elemento encontrado, o null si no está en la lista.
     */
    public T obtener(T buscado) {
        ElementoSE<T> puntero = cabeza; // Puntero que recorre la lista
        while(puntero!=null){ // Se recorre hasta que el último se acabe la lista
            if(puntero.elemento.equals(buscado)){
                return puntero.elemento; // Si se encuentra el elemento pedido se devuelve
            }
            puntero = puntero.siguiente;
        }
        return null; // Si no se encuentra el elemento pedido se devuelve null
    }


    /**
     * Añade un elemento a la lista.
     * @param elemento Elemento que se desea insertar.
     */
    public void add(T elemento) {
        ElementoSE<T> nuevo = new ElementoSE<>(elemento); // Creamos un nuevo elemento de la clase EstructurasDeDatos.ElementoSE
        if(cabeza == null){ // Si la lista está vacía, el nuevo elemento es el primero
            cabeza = nuevo;
        }
        else{ // Si no, se recorre la lista hasta llegar al último
            ElementoSE<T> puntero = cabeza; // Puntero que recorre la lista
            while(puntero.siguiente != null){
                puntero = puntero.siguiente;
            }
            puntero.siguiente = nuevo; // Se le asigna el nuevo al siguiente del último

        }
        tamaño ++; // Incrementamos el tamaño de la lista en 1

    }


    /**
     * Comprueba si la lista está vacía.
     * @return true si no contiene elementos, false en caso contrario.
     */
    public boolean vacio() {
        return(cabeza == null);
    }

    /**
     * Devuelve el número de elementos que hay actualmente en la lista.
     * @return Tamaño de la lista.
     */
    public int size() {
        return tamaño;
    }


    /**
     * Devuelve un iterador para recorrer la lista.
     * Permite acceder secuencialmente a los elementos sin exponer
     * la implementación interna de la lista.
     * @return Iterador que permite recorrer la lista.
     */
    public ContratoIterador<T> Iterador() {
        return new IteradorLSE<>(this.cabeza);
    }

    public void insertarEnPosicion(T elemento, int pos) {

        ElementoSE<T> nuevo = new ElementoSE<>(elemento);
        // Caso 1: insertar al inicio
        if (pos <= 0 || cabeza == null) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        else{
            ElementoSE<T> actual = cabeza;
            int contador = 0;
            // Recorremos hasta la posición anterior
            while (actual.siguiente != null && contador < pos -1) {
                actual = actual.siguiente;
                contador++;
            }
            // Insertamos
            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;

        }
        tamaño++;
    }
    public T get(int pos) {
        // Si la lista está vacía o la posición es inválida
        if (cabeza == null || pos < 0 || pos >= tamaño) {
            return null;
        }

        ElementoSE<T> actual = cabeza;
        int contador = 0;

        // Recorremos hasta llegar al índice indicado
        while (actual != null && contador < pos) {
            actual = actual.siguiente; // O actual.siguiente según tus nombres
            contador++;
        }

        // Si llegamos a la posición, devolvemos el dato del elemento
        return (actual != null) ? actual.elemento : null;
    }
}