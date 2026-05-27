package modelo;

public class Puerta extends Objeto {
    private String llaveId;
    private int habitacionDestinoId;
    private boolean abierta;

    public Puerta(String nombre, String descripcion, String llaveId, int habitacionDestinoId) {
        super(nombre, descripcion);
        this.llaveId = llaveId;
        this.habitacionDestinoId = habitacionDestinoId;
        this.abierta = false;
    }

    @Override
    public void usar(Jugador jugador) {
        // La lógica de abrir la puerta se maneja en InteraccionService
        // pero implementamos el método para cumplir el contrato de Objeto.
    }

    public String getRequiredKeyId() {
        return llaveId;
    }

    public int getHabitacionDestinoId() {
        return habitacionDestinoId;
    }

    public boolean isAbierta() {
        return abierta;
    }

    public void abrir() {
        this.abierta = true;
    }

    public void unlock() {
        this.llaveId = null;
    }
}
