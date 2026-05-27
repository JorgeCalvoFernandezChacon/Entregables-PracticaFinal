package logica;

import modelo.*;
import excepciones.AccionInvalidaException;

public class InteraccionService {
    private CombateService combateService;

    public InteraccionService() {
        this.combateService = new CombateService();
    }

    public void recogerObjeto(Jugador jugador, Celda celda) throws AccionInvalidaException {
        if (jugador == null || celda == null) return;
        
        Objeto objeto = celda.getObjeto();
        if (objeto == null) {
            throw new AccionInvalidaException("La celda no contiene ningún objeto.");
        }
        if (objeto instanceof Puerta) {
            throw new AccionInvalidaException("No puedes recoger una puerta.");
        }
        if (objeto instanceof Equipable) {
            Equipable old = jugador.equipar((Equipable) objeto);
            celda.setObjeto(null);
            if (old != null) {
                celda.setObjeto(old);
                SistemaLog.getInstance().registrarEvento("🗡️ Dejaste caer " + old.getNombre() + " en el suelo.");
            }
            SistemaLog.getInstance().registrarEvento("🧙 equipó 🗡️ " + objeto.getNombre() + ".");
        } else if (jugador.getInventario().añadirObjeto(objeto)) {
            celda.setObjeto(null);
            String emoji = "📦";
            if (objeto instanceof Consumible) emoji = "🧪";
            else if (objeto instanceof Llave) emoji = "🔑";
            else if (objeto instanceof Trampa) emoji = "⚠️";
            SistemaLog.getInstance().registrarEvento("🧙 recogió " + emoji + " " + objeto.getNombre() + ".");
        }
    }

    public void usarObjeto(Jugador jugador, Objeto objeto) throws AccionInvalidaException {
        if (jugador == null || objeto == null) return;
        
        Inventario inv = jugador.getInventario();
        boolean contains = false;
        for (int i = 0; i < inv.getTamaño(); i++) {
            if (inv.obtenerObjeto(i) == objeto) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            throw new AccionInvalidaException("El inventario no contiene el objeto.");
        }
        
        if (!(objeto instanceof Consumible)) {
            throw new AccionInvalidaException("El objeto no es consumible.");
        }
        
        objeto.usar(jugador);
        String emojiUsar = "📦";
        if (objeto instanceof Consumible) emojiUsar = "🧪";
        else if (objeto instanceof Equipable) emojiUsar = "🗡️";
        SistemaLog.getInstance().registrarEvento("🧙 usó " + emojiUsar + " " + objeto.getNombre() + ".");
        
        jugador.getInventario().eliminarObjeto(objeto);
    }

    public int atacarEnemigo(Jugador jugador, Enemigo enemigo) throws AccionInvalidaException {
        if (jugador == null || enemigo == null) {
            throw new AccionInvalidaException("No hay un enemigo en la posición objetivo.");
        }
        if (enemigo.isMuerto()) {
            throw new AccionInvalidaException("El enemigo ya está muerto.");
        }
        return combateService.ejecutarAtaque(jugador, enemigo);
    }

    public void abrirPuerta(Jugador jugador, Puerta puerta, Habitacion nuevaHabitacion, Celda celdaActual) throws AccionInvalidaException {
        if (jugador == null || puerta == null) return;
        
        if (puerta.isAbierta()) {
            throw new AccionInvalidaException("La puerta ya está abierta.");
        }
        
        if (puerta.getRequiredKeyId() != null) {
            throw new AccionInvalidaException("La puerta tiene un candado 🔒. Usa una llave 🔑 estando en esta casilla.");
        }
        
        puerta.abrir();
        jugador.setHabitacionId(puerta.getHabitacionDestinoId());
        
        // Clean up old cell and place player in new room
        celdaActual.setEntidad(null);
        jugador.setHabitacion(nuevaHabitacion);
        jugador.setFila(0);
        jugador.setColumna(0);
        Celda destino = nuevaHabitacion.getCelda(0, 0);
        if (destino != null && !destino.hasEntidad()) {
            destino.setEntidad(jugador);
        } else {
            // Fallback: find first free cell
            for (int f = 0; f < nuevaHabitacion.getFilas(); f++) {
                for (int c = 0; c < nuevaHabitacion.getColumnas(); c++) {
                    Celda celda = nuevaHabitacion.getCelda(f, c);
                    if (celda != null && !celda.hasEntidad()) {
                        jugador.setFila(f);
                        jugador.setColumna(c);
                        celda.setEntidad(jugador);
                        return;
                    }
                }
            }
            throw new AccionInvalidaException("No hay espacio libre en la nueva habitación.");
        }
    }
}
