package logica;

import estructuras.ListaEnlazada;

public class SistemaLog {
    private static SistemaLog instance;
    private ListaEnlazada<String> eventos;

    private SistemaLog() {
        this.eventos = new ListaEnlazada<>();
    }

    public static SistemaLog getInstance() {
        if (instance == null) {
            instance = new SistemaLog();
        }
        return instance;
    }

    public void registrarEvento(String evento) {
        eventos.add(evento);
    }

    public ListaEnlazada<String> obtenerLogCompleto() {
        return eventos;
    }

    public void limpiar() {
        eventos = new ListaEnlazada<>();
    }
}
