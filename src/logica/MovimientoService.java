package logica;

import modelo.*;
import estructuras.*;
import excepciones.*;

public class MovimientoService {

    /**
     * Calculates the cells reachable by the player in the current turn based on their speed.
     * 
     * @param jugador The player entity.
     * @param habitacion The room the player is currently in.
     * @return A ListaEnlazada of reachable Posicion objects.
     */
    public static ListaEnlazada<Posicion> obtenerCeldasAlcanzables(Jugador jugador, Habitacion habitacion) {
        ListaEnlazada<Posicion> alcanzables = new ListaEnlazada<>();
        int startRow = jugador.getFila();
        int startCol = jugador.getColumna();
        int velocidad = jugador.getVelocidad();
        
        int rows = habitacion.getFilas();
        int cols = habitacion.getColumnas();
        
        // Build the grid for BFS: 0 = walkable, 1 = blocked
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Celda celda = habitacion.getCelda(r, c);
                // A cell is blocked if it has an entity and it's not the player
                if (celda.hasEntidad() && celda.getEntidad() != jugador) {
                    grid[r][c] = 1;
                } else {
                    grid[r][c] = 0;
                }
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // The current position is typically not considered a "reachable" cell for movement selection
                if (r == startRow && c == startCol) continue;

                ListaEnlazada<Integer> path = BFS.findShortestPathGrid(grid, startRow, startCol, r, c);
                if (path != null && (path.size() - 1) <= velocidad) {
                    alcanzables.add(new Posicion(r, c));
                }
            }
        }
        
        return alcanzables;
    }

    /**
     * Moves the player to a new position and updates the room's state.
     * 
     * @param jugador The player entity.
     * @param habitacion The room the player is in.
     * @param destino The destination position.
     * @throws MovimientoInvalidoException if the destination is not reachable or occupied.
     */
    public static void moverJugador(Jugador jugador, Habitacion habitacion, Posicion destino) throws MovimientoInvalidoException {
        Celda newCelda = habitacion.getCelda(destino.fila, destino.columna);
        if (newCelda == null) {
            throw new MovimientoInvalidoException("Posición de destino fuera de los límites de la habitación.");
        }
        if (newCelda.hasEntidad()) {
            throw new MovimientoInvalidoException("La celda de destino ya está ocupada por otra entidad.");
        }

        // Update the room's cells
        Celda oldCelda = habitacion.getCelda(jugador.getFila(), jugador.getColumna());
        if (oldCelda != null) {
            oldCelda.setEntidad(null);
        }

        jugador.setHabitacion(habitacion);
        jugador.setFila(destino.fila);
        jugador.setColumna(destino.columna);

        try {
            habitacion.colocarEntidad(jugador, destino.fila, destino.columna);
        } catch (IllegalArgumentException e) {
            throw new MovimientoInvalidoException(e.getMessage());
        }
        SistemaLog.getInstance().registrarEvento("🧙 se movió a (" + destino.fila + ", " + destino.columna + ").");
        
        if (newCelda.hasObjeto()) {
            Objeto obj = newCelda.getObjeto();
            if (obj instanceof Trampa) {
                ((Trampa) obj).usar(jugador);
                newCelda.setObjeto(null);
            }
        }
    }
}
