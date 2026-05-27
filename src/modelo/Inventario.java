package modelo;

import estructuras.ListaEnlazada;

public class Inventario {
    private ListaEnlazada<Objeto> objetos;
    private int capacidadMaxima;

    public Inventario(int capacidadMaxima) {
        this.objetos = new ListaEnlazada<>();
        this.capacidadMaxima = capacidadMaxima;
    }

    public boolean añadirObjeto(Objeto obj) {
        if (estáLleno()) {
            return false;
        }
        objetos.add(obj);
        return true;
    }

    public boolean eliminarObjeto(Objeto obj) {
        return objetos.remove(obj);
    }

    public Objeto obtenerObjeto(int index) {
        return objetos.get(index);
    }

    public boolean estáLleno() {
        return objetos.size() >= capacidadMaxima;
    }

    public int getTamaño() {
        return objetos.size();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
}
