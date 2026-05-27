package estructuras;

public class IteradorC<T> implements ContratoIterador<T> {
    private ElementoC<T> apuntado;
    private int recorridos;
    private int tamano;

    public IteradorC(ElementoC<T> comienzo, int tamano) {
        this.apuntado = comienzo;
        this.recorridos = 0;
        this.tamano = tamano;
    }

    @Override
    public boolean haySiguiente() {
        return (recorridos < tamano);
    }

    @Override
    public T siguiente() {
        T elemento = apuntado.elemento;
        apuntado = apuntado.siguiente;
        recorridos++;
        return elemento;
    }
}
