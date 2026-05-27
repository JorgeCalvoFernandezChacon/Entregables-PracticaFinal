package modelo;

public abstract class Entidad {
    protected int vida;
    protected int ataque;
    protected int defensa;
    protected int fila;
    protected int columna;
    protected boolean muerto = false;

    public Entidad(int vida, int ataque, int defensa, int fila, int columna) {
        this.vida = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.fila = fila;
        this.columna = columna;
    }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = Math.max(0, vida); }

    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }

    public boolean isMuerto() { return muerto; }
    public void setMuerto(boolean muerto) { this.muerto = muerto; }

    public boolean isVivo() {
        return vida > 0;
    }

    public abstract String actuar(Habitacion habitacion, Jugador jugador);
}
