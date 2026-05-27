package modelo;

public class Llave extends Objeto {
    private String puertaId;

    public Llave(String nombre, String descripcion, String puertaId) {
        super(nombre, descripcion);
        this.puertaId = puertaId;
    }

    public String getPuertaId() {
        return puertaId;
    }

    @Override
    public void usar(Jugador jugador) {
        // Logic to open door will be in the controller or Habitación/Grafo
    }
}
