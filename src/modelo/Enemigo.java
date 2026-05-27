package modelo;

public class Enemigo extends Entidad {
    private int velocidad;

    public Enemigo(int vida, int ataque, int defensa, int fila, int columna, int velocidad) {
        super(vida, ataque, defensa, fila, columna);
        this.velocidad = velocidad;
    }

    public int getVelocidad() { return velocidad; }

    @Override
    public String actuar(Habitacion habitacion, Jugador jugador) {
        if (isAdyacente(jugador)) {
            return atacar(jugador);
        } else {
            moverseHacia(jugador, habitacion);
            return null;
        }
    }

    private boolean isAdyacente(Jugador jugador) {
        int df = Math.abs(this.fila - jugador.getFila());
        int dc = Math.abs(this.columna - jugador.getColumna());
        return df + dc == 1;
    }

    private String atacar(Jugador jugador) {
        double random = Math.random();
        int dañoBruto = (int) (this.ataque * random * 2);
        int defensaTotal = jugador.getDefensaTotal();
        int daño = Math.max(0, dañoBruto - defensaTotal);
        jugador.setVida(jugador.getVida() - daño);
        if (daño > 0) {
            return "👹 te atacó (" + dañoBruto + " atq - " + defensaTotal + " def) → 💔 -" + daño + " vida";
        } else {
            return "👹 te atacó pero no logró hacer daño (" + dañoBruto + " atq vs " + defensaTotal + " def) 🛡️";
        }
    }

    private void moverseHacia(Jugador jugador, Habitacion habitacion) {
        int nuevaFila = this.fila;
        int nuevaColumna = this.columna;

        int df = jugador.getFila() - this.fila;
        int dc = jugador.getColumna() - this.columna;
        if (Math.abs(df) >= Math.abs(dc)) {
            if (df > 0) nuevaFila++;
            else if (df < 0) nuevaFila--;
        } else {
            if (dc > 0) nuevaColumna++;
            else if (dc < 0) nuevaColumna--;
        }

        Celda celdaDestino = habitacion.getCelda(nuevaFila, nuevaColumna);
        if (celdaDestino != null && !celdaDestino.hasEntidad()) {
            Celda celdaActual = habitacion.getCelda(this.fila, this.columna);
            if (celdaActual != null) {
                celdaActual.setEntidad(null);
            }
            this.fila = nuevaFila;
            this.columna = nuevaColumna;
            celdaDestino.setEntidad(this);
        }
    }
}
