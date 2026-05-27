package modelo;

public abstract class Objeto {
    private String nombre;
    private String descripcion;

    public Objeto(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public abstract void usar(Jugador jugador);

    @Override
    public String toString() {
        return nombre + ": " + descripcion;
    }
}
