package logica;

import modelo.*;
import estructuras.*;
import excepciones.*;
import persistencia.*;

public class CasosDeUsoRunner {

    public static void main(String[] args) {
        System.out.println("--- Ejecutando Casos de Uso de la Tarea T16 ---");

        testMoverJugador();
        testInteraccion();
        testCombate();
        testPersistencia();

        System.out.println("--- Pruebas Finalizadas ---");
    }

    private static void testMoverJugador() {
        System.out.println("\n[Test: Mover Jugador]");
        Habitacion hab = new Habitacion(5, 5);
        Jugador jugador = new Jugador(100, 20, 10, 2, 0, 0);
        jugador.setHabitacion(hab);
        hab.colocarEntidad(jugador, 0, 0);

        // Caso 1: Movimiento válido
        try {
            Posicion destinoValido = new Posicion(0, 1);
            MovimientoService.moverJugador(jugador, hab, destinoValido);
            System.out.println("Éxito: Jugador se movió a (0, 1).");
        } catch (MovimientoInvalidoException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        // Caso 2: Movimiento a celda ocupada
        Enemigo enemigo = new Enemigo(50, 10, 5, 0, 2, 4);
        hab.colocarEntidad(enemigo, 0, 2);
        try {
            Posicion destinoOcupado = new Posicion(0, 2);
            MovimientoService.moverJugador(jugador, hab, destinoOcupado);
            System.out.println("Error: El jugador se movió a una celda ocupada (no debería).");
        } catch (MovimientoInvalidoException e) {
            System.out.println("Éxito: Lanzada MovimientoInvalidoException al intentar mover a celda ocupada: " + e.getMessage());
        }

        // Caso 3: Movimiento fuera de límites
        try {
            Posicion destinoFuera = new Posicion(6, 6);
            MovimientoService.moverJugador(jugador, hab, destinoFuera);
            System.out.println("Error: El jugador se movió fuera de los límites (no debería).");
        } catch (MovimientoInvalidoException e) {
            System.out.println("Éxito: Lanzada MovimientoInvalidoException al intentar mover fuera de límites: " + e.getMessage());
        }
    }

    private static void testInteraccion() {
        System.out.println("\n[Test: Interacción]");
        Habitacion hab = new Habitacion(5, 5);
        Jugador jugador = new Jugador(100, 20, 10, 2, 0, 0);
        InteraccionService interaccion = new InteraccionService();

        // Caso 1: Recoger objeto inexistente
        Celda celdaVacia = hab.getCelda(1, 1);
        try {
            interaccion.recogerObjeto(jugador, celdaVacia);
            System.out.println("Error: El jugador recogió un objeto inexistente (no debería).");
        } catch (AccionInvalidaException e) {
            System.out.println("Éxito: Lanzada AccionInvalidaException al recoger objeto inexistente: " + e.getMessage());
        }

        // Caso 2: Recoger objeto existente
        Consumible pocion = new Consumible("Poción de Vida", "Restaura vida", 20);
        celdaVacia.setObjeto(pocion);
        try {
            interaccion.recogerObjeto(jugador, celdaVacia);
            if (jugador.getInventario().getTamaño() > 0) {
                System.out.println("Éxito: Jugador recogió la poción correctamente.");
            } else {
                System.out.println("Error: La poción no se añadió al inventario.");
            }
        } catch (AccionInvalidaException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    private static void testCombate() {
        System.out.println("\n[Test: Combate]");
        Jugador jugador = new Jugador(100, 50, 10, 2, 0, 0);
        Enemigo enemigo = new Enemigo(30, 10, 5, 0, 1, 4);
        CombateService combate = new CombateService();

        int vidaInicialEnemigo = enemigo.getVida();
        combate.ejecutarAtaque(jugador, enemigo);
        
        if (enemigo.getVida() < vidaInicialEnemigo) {
            System.out.println("Éxito: Vida del enemigo reducida tras ataque. Vida actual: " + enemigo.getVida());
        } else {
            System.out.println("Error: La vida del enemigo no disminuyó.");
        }

        // Ataque hasta la muerte
        while (!enemigo.isMuerto()) {
            combate.ejecutarAtaque(jugador, enemigo);
        }
        if (enemigo.isMuerto()) {
            System.out.println("Éxito: Enemigo derrotado.");
        }
    }

    private static void testPersistencia() {
        System.out.println("\n[Test: Persistencia]");
        Jugador jugador = new Jugador(100, 20, 10, 2, 0, 0);
        jugador.setVida(75);
        JsonPersistencia persistencia = new JsonPersistencia();
        String fileName = "save_test.json";

        try {
            persistencia.guardarEstado(fileName, jugador, 50);
            System.out.println("Éxito: Estado guardado en " + fileName);

            Jugador jugadorCargado = new Jugador(100, 20, 10, 2, 0, 0);
            persistencia.cargarEstado(fileName, jugadorCargado);
            
            if (jugadorCargado.getVida() == 75) {
                System.out.println("Éxito: Estado cargado correctamente. Vida: " + jugadorCargado.getVida());
            } else {
                System.out.println("Error: Vida cargada no coincide. Esperado: 75, Obtenido: " + jugadorCargado.getVida());
            }
        } catch (JsonException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
        }
    }
}
