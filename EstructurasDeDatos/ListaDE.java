package EstructurasDeDatos;

/**
 * Lista doblemente enlazada (LDE)
 * Permite almacenar elementos comparables y realizar operaciones sobre la lista
 * como añadir, eliminar u obtener elementos, o saber el tamaño de la lista.
 * @param <T> Es un parámetro de typo, con la restricción que tiene que ser comparable
 *           para el caso en que queramos hacer listas ordenadas
 * La estructura está formada por nodos enlazados con referencias al siguiente elemento y al anterior.
 */
public class ListaDE<T extends Comparable<T>> implements ContratoListas<T>{
    protected ElementoDE<T> primero; // Referencia al primer elemento de la lista, protected para que las subclases puedan acceder a él (LDEO).
    protected ElementoDE<T> ultimo; // Referencia al último elemento de la lista, protected para que las subclases puedan acceder a él.
    protected int tamaño = 0; // En un principio la lista está vacía

    @Override
    /**
     * Elimina un elemento de la lista.
     * @param elemento Elemento que se desea eliminar.
     * @return El elemento eliminado si existía en la lista,
     *         o null si no se encontraba.
     */
    public T eliminar(T elemento) {
        ElementoDE<T> apuntado = primero; // Puntero que recorre la lista
        while(apuntado != null){ // Recorremos la lista hasta que el último elemento sea null.
            if(apuntado.elemento.compareTo(elemento)==0){
                if(apuntado.anterior == null){ // Si es el primer elemento, el primero pasa a ser el siguiente
                    primero = apuntado.siguiente;
                }
                else{ //Si no es el primer elemento, al anterior se le asigna el siguiente
                    apuntado.anterior.siguiente = apuntado.siguiente;
                }
                if(apuntado.siguiente == null){ // Si es el último elemento, al anterior se le asigna el valor de último
                    ultimo = apuntado.anterior;
                }
                else{ // Si no es el último elemento, al siguiente se le asigna la referencia del anterior
                    apuntado.siguiente.anterior = apuntado.anterior;
                }
                tamaño --; // Se reduce el tamaño de la lista en uno
                return apuntado.elemento;
            }
            apuntado = apuntado.siguiente;
        }
        return null;
    }

    @Override
    /**
     * Obtiene un elemento de la lista.
     * @param elemento Elemento que se desea buscar.
     * @return El elemento encontrado, o null si no está en la lista.
     */
    public T obtener(T elemento) {
        ElementoDE<T> apuntado = primero; // Puntero que recorre la lista
        while(apuntado != null){ // Recorremos la lista hasta que se acabe
            if(apuntado.elemento.compareTo(elemento)==0){
                return apuntado.elemento; // Si encontramos el elemento pedido se devuelve
            }
            apuntado = apuntado.siguiente;
        }
        return null; // Si no se encuentra el elemento, se devuelve null.
    }

    @Override
    /**
     * Añade un elemento a la lista.
     * @param elemento Elemento que se desea insertar.
     */
    public void añadir(T elemento) {
        ElementoDE<T> nuevo = new ElementoDE<>(elemento); // Creamos un nuevo elemento de la clase EstructurasDeDatos.ElementoDE
        if(tamaño==0){
            primero = nuevo;
            ultimo = nuevo; // Si la lista está vacía, el primero y el último pasan a ser el nuevo
        }
        else{ // Si no, se añade
            ultimo.siguiente = nuevo; // El siguiente del último es el nuevo
            nuevo.anterior = ultimo; // El anterior del nuevo es el último
            ultimo = nuevo; // El nuevo pasa a ser el último
        }
        tamaño ++; // Ampliamos tamaño en uno
    }

    @Override
    /**
     * Comprueba si la lista está vacía.
     * @return true si no contiene elementos, false en caso contrario.
     */
    public boolean vacio() {
        return (tamaño==0);
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
        return new IteradorLDE<>(this.primero);
    }
}