package modelo;

public class Consumible extends Objeto {
    private int recuperacionVida;

    public Consumible(String nombre, String descripcion, int recuperacionVida) {
        super(nombre, descripcion);
        this.recuperacionVida = recuperacionVida;
    }

    public int getRecuperacionVida() {
        return recuperacionVida;
    }

    @Override
    public void usar(Jugador jugador) {
        jugador.curar(recuperacionVida);
        // The object is destroyed after use, which will be handled by the inventory removal logic
    }
}
