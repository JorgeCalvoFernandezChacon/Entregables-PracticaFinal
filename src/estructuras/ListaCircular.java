package estructuras;

public class ListaCircular<T> implements ContratoListas<T> {
    private ElementoC<T> primero;
    private ElementoC<T> ultimo;
    private ElementoC<T> cursor;
    private int tamano = 0;

    @Override
    public void añadir(T elemento) {
        ElementoC<T> nuevo = new ElementoC<>(elemento);
        if (tamano == 0) {
            primero = nuevo;
            ultimo = nuevo;
            primero.siguiente = primero;
            primero.anterior = primero;
        } else {
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            ultimo = nuevo;
            ultimo.siguiente = primero;
            primero.anterior = ultimo;
        }
        tamano++;
    }

    public void add(T elemento) { añadir(elemento); }

    @Override
    public T eliminar(T elemento) {
        if (tamano == 0 || elemento == null) return null;
        if (primero.elemento.equals(elemento)) {
            T dato = primero.elemento;
            if (tamano == 1) {
                primero = null;
                ultimo = null;
                cursor = null;
            } else {
                primero = primero.siguiente;
                primero.anterior = ultimo;
                ultimo.siguiente = primero;
            }
            tamano--;
            return dato;
        }
        ElementoC<T> apuntado = primero.siguiente;
        while (apuntado != primero) {
            if (apuntado.elemento.equals(elemento)) {
                T dato = apuntado.elemento;
                if (apuntado == ultimo) {
                    ultimo = ultimo.anterior;
                    ultimo.siguiente = primero;
                    primero.anterior = ultimo;
                } else {
                    apuntado.anterior.siguiente = apuntado.siguiente;
                    apuntado.siguiente.anterior = apuntado.anterior;
                }
                tamano--;
                return dato;
            }
            apuntado = apuntado.siguiente;
        }
        return null;
    }

    public boolean remove(T data) {
        return eliminar(data) != null;
    }

    @Override
    public T obtener(T elemento) {
        if (tamano == 0) return null;
        if (primero.elemento.equals(elemento)) return primero.elemento;
        ElementoC<T> apuntado = primero.siguiente;
        while (apuntado != primero) {
            if (apuntado.elemento.equals(elemento)) return apuntado.elemento;
            apuntado = apuntado.siguiente;
        }
        return null;
    }

    @Override
    public boolean vacio() {
        return (tamano == 0);
    }

    @Override
    public int tamañoLista() {
        return tamano;
    }

    @Override
    public ContratoIterador<T> Iterador() {
        return new IteradorC<>(primero, tamano);
    }

    // Cursor: allows cycling through the list
    public T next() {
        if (tamano == 0) throw new RuntimeException("Circular list is empty");
        if (cursor == null) cursor = primero;
        else cursor = cursor.siguiente;
        return cursor.elemento;
    }

    public T peek() {
        if (tamano == 0) return null;
        if (cursor == null) return primero.elemento;
        return cursor.elemento;
    }

    public boolean isEmpty() { return vacio(); }
    public int size() { return tamañoLista(); }
}
