package modelo;

public class Trampa extends Objeto {
    private int daño;

    public Trampa(String nombre, String descripcion, int daño) {
        super(nombre, descripcion);
        this.daño = daño;
    }

    public int getDaño() {
        return daño;
    }

    @Override
    public void usar(Jugador jugador) {
        int vidaActual = jugador.getVida();
        jugador.setVida(vidaActual - daño);
    }
}
