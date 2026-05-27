package logica;

import modelo.*;
import estructuras.ListaCircular;
import estructuras.ListaEnlazada;

public class GestorTurnos {
    private int turnosRestantes;
    private Jugador jugador;

    public GestorTurnos(int turnosIniciales, Jugador jugador) {
        this.turnosRestantes = turnosIniciales;
        this.jugador = jugador;
    }

    public void finalizarTurno() {
        turnosRestantes--;
    }

    public ListaEnlazada<String> procesarEnemigos(Habitacion habitacion) {
        ListaEnlazada<String> mensajes = new ListaEnlazada<>();
        // ListaCircular para gestionar el orden de turnos de los enemigos.
        // Se insertan ordenados por velocidad (mayor velocidad = actúa primero).
        ListaCircular<Enemigo> colaEnemigos = new ListaCircular<>();

        for (int r = 0; r < habitacion.getFilas(); r++) {
            for (int c = 0; c < habitacion.getColumnas(); c++) {
                Celda celda = habitacion.getCelda(r, c);
                if (celda.hasEntidad() && celda.getEntidad() instanceof Enemigo) {
                    Enemigo e = (Enemigo) celda.getEntidad();
                    if (e.isVivo()) {
                        colaEnemigos.add(e);
                    }
                }
            }
        }

        // Procesar todos los enemigos recorriendo la lista circular
        int total = colaEnemigos.size();
        for (int i = 0; i < total; i++) {
            Enemigo e = colaEnemigos.next();
            String msg = e.actuar(habitacion, jugador);
            if (msg != null) {
                mensajes.add(msg);
                SistemaLog.getInstance().registrarEvento(msg);
            }
        }
        return mensajes;
    }

    public boolean isDerrotaPorTurnos() {
        return turnosRestantes <= 0;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }
}
