package estructuras;

public class IteradorPila<T> implements ContratoIterador<T> {
    private ElementoPila<T> apuntado;

    public IteradorPila(ElementoPila<T> cima) {
        this.apuntado = cima;
    }

    @Override
    public boolean haySiguiente() {
        return (apuntado != null);
    }

    @Override
    public T siguiente() {
        if (!haySiguiente()) return null;
        T elemento = apuntado.elemento;
        apuntado = apuntado.siguiente;
        return elemento;
    }
}
