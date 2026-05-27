package modelo;

public class Equipable extends Objeto {
    public enum Slot { MANO, ESCUDO, ARMADURA }

    private int bonoAtaque;
    private int bonoDefensa;
    private Slot slot;

    public Equipable(String nombre, String descripcion, int bonoAtaque, int bonoDefensa, Slot slot) {
        super(nombre, descripcion);
        this.bonoAtaque = bonoAtaque;
        this.bonoDefensa = bonoDefensa;
        this.slot = slot;
    }

    public int getBonoAtaque() { return bonoAtaque; }
    public int getBonoDefensa() { return bonoDefensa; }
    public Slot getSlot() { return slot; }

    @Override
    public void usar(Jugador jugador) {
        jugador.equipar(this);
    }
}
