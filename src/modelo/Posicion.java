package modelo;

public class Posicion {
    public final int fila;
    public final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posicion posicion = (Posicion) o;
        return fila == posicion.fila && columna == posicion.columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "(" + fila + ", " + columna + ")";
    }
}
