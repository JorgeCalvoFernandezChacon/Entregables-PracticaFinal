package EstructurasDeDatos;

public class ListaSEO<T extends Comparable<T>> extends ListaSE<T> {
    public void add(T elemento){
        ElementoSE<T> nuevo = new ElementoSE<>(elemento);
        if(cabeza == null || cabeza.elemento.compareTo(elemento) > 0){
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        }
        else{
            ElementoSE<T> apuntado = cabeza;
            while(apuntado.siguiente != null && apuntado.siguiente.elemento.compareTo(elemento) < 0){
                apuntado = apuntado.siguiente;
            }
            nuevo.siguiente = apuntado.siguiente;
            apuntado.siguiente = nuevo;
        }
        tamaño ++;
    }
}