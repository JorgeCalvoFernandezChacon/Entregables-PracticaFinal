package modelo;

public class Habitacion {
    private final int filas;
    private final int columnas;
    private final Celda[][] matriz;

    public Habitacion(int filas, int columnas) {
        if (filas > 10 || columnas > 10) {
            throw new IllegalArgumentException("La habitación no puede superar los 10x10");
        }
        this.filas = filas;
        this.columnas = columnas;
        this.matriz = new Celda[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz[i][j] = new Celda();
            }
        }
    }

    public Celda getCelda(int f, int c) {
        if (f < 0 || f >= filas || c < 0 || c >= columnas) {
            return null;
        }
        return matriz[f][c];
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void colocarEntidad(Entidad e, int f, int c) {
        Celda celda = getCelda(f, c);
        if (celda == null) {
            throw new IllegalArgumentException("Posición fuera de los límites de la habitación.");
        }
        if (celda.hasEntidad()) {
            throw new IllegalArgumentException("La celda ya está ocupada por otra entidad.");
        }
        celda.setEntidad(e);
    }
}
