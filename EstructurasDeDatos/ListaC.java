package EstructurasDeDatos;

/**
 * Lista circular (LC)
 * Permite almacenar elementos comparables y realizar operaciones sobre la lista
 * como añadir, eliminar u obtener elementos, o saber el tamaño de la lista.
 * @param <T> Es un parámetro de typo, con la restricción que tiene que ser comparable
 *           para el caso en que queramos hacer listas ordenadas
 * La estructura está formada por nodos enlazados con referencias al siguiente elemento y al anterior
 * con la distinción de que el último elemento tiene la referencia del primero y el primero la del último.
 */
public class ListaC<T extends Comparable<T>> implements ContratoListas<T>{
    private ElementoC<T> primero; // Referencia al primer elemento de la lista.
    private ElementoC<T> ultimo; // Referencia al último elemento de la lista.
    private int tamaño = 0; // En un principio la lista está vacía.
    @Override
    /**
     * Elimina un elemento de la lista.
     * @param elemento Elemento que se desea eliminar.
     * @return El elemento eliminado si existía en la lista,
     *         o null si no se encontraba.
     */
    public T eliminar(T elemento) {
        ElementoC<T> apuntado = primero.siguiente; // Puntero que recorre la lista empezando por el elemento siguiente al primero
        if (tamaño == 0) return null; // Si la lista está vacía no se puede eliminar el elemento
        if (primero.elemento.compareTo(elemento) == 0) { // Se comprueba si el elemento a eliminar es el primero, manualmente.
            if (tamaño == 1) { // Si es el único elemento de la lista, la lista pasa a ser vacía
                primero = null;
                ultimo = null;
            } else {
                primero = primero.siguiente; // Si no, el primero pasa a ser el siguiente
                primero.anterior = ultimo; // El anterior del nuevo primero es el último
                ultimo.siguiente = primero; // El siguiente del último es el nuevo primero
            }
            while (apuntado != primero) { // Se recorre la lista hasta que lleguemos al primero elemento, como es circular, no tiene fina
                if (apuntado.elemento.compareTo(elemento) == 0) {
                    if (apuntado == ultimo) { // Si es el último elemento de la lista
                        ultimo = ultimo.anterior; // El anterior pasa a ser el nuevo último
                        ultimo.siguiente = primero; // El siguiente del nuevo último pasa a ser el primero
                        primero.anterior = ultimo; // EL anterior del primero pasa a ser el nuevo último
                    } else { // Si no es el último, se asigna al anterior el siguiente y viceversa
                        apuntado.anterior.siguiente = apuntado.siguiente;
                        apuntado.siguiente.anterior = apuntado.anterior;
                    }
                    tamaño--; // Disminuimos en uno el tamaño
                    return elemento;
                }
                apuntado = apuntado.siguiente;
            }
        }
        return null;
    }

    @Override
    /**
     * Obtiene un elemento de la lista.
     * @param elemento Elemento que se desea buscar.
     * @return El elemento encontrado, o null si no está en la lista.
     */
    public T obtener(T elemento){
            if(tamaño == 0) return null; // Si la lista está vacía se devuelve null
            if(primero.elemento.compareTo(elemento) == 0){
                return primero.elemento; // Se comprueba el primero manualmente
            }
            ElementoC<T> apuntado = primero.siguiente; // Creamos un puntero que empiece en el siguiente del primero
            while(apuntado != primero){ // Recorremos la lista hasta llegar al último
                if(apuntado.elemento.compareTo(elemento) == 0){
                    return apuntado.elemento; // Si se encuentra el elemento pedido, se devuelve
                }
                apuntado = apuntado.siguiente;
            }

            return null;
        }

    @Override
    /**
     * Añade un elemento a la lista.
     * @param elemento Elemento que se desea insertar.
     */
    public void añadir(T elemento) {
        ElementoC<T> nuevo = new ElementoC<>(elemento); // Creamos un nuevo elemento de la clase EstructurasDeDatos.ElementoC
        if(tamaño == 0){ // Si la lista es vacía
           primero = nuevo; // El nuevo es el primero y el último
           ultimo = nuevo;
           primero.siguiente = primero; // Se apunta a sí mismo, tanto como anterior como siguiente
           primero.anterior = primero;
        }
        else{ // Si la lista no es vacía, lo añadimos con la referencia del último elemento
            ultimo.siguiente = nuevo; // El siguiente del último es el nuevo
            nuevo.anterior = ultimo; // El anterior del nuevo es el último
            ultimo = nuevo; // El nuevo pasa a ser el último
            ultimo.siguiente = primero; // El siguiente del último es el primero
            primero.anterior = ultimo; // El anterior del primero es el último
        }
        tamaño ++; // Incrementamos el tamaño en 1
    }

    @Override
    /**
     * Comprueba si la lista está vacía.
     * @return true si no contiene elementos, false en caso contrario.
     */
    public boolean vacio() {
        return (tamaño == 0);
    }

    @Override
    /**
     * Devuelve el número de elementos que hay actualmente en la lista.
     * @return Tamaño de la lista.
     */
    public int tamañoLista() {
        return tamaño;
    }

    @Override
    /**
     * Devuelve un iterador para recorrer la lista.
     * Permite acceder secuencialmente a los elementos sin exponer
     * la implementación interna de la lista.
     * @return Iterador que permite recorrer la lista.
     */
    public ContratoIterador<T> Iterador() {
        return new IteradorC<>(this.primero, tamaño);
    }
}