package modelo;

public class Celda {
    private Entidad entidad;
    private Objeto objeto;

    public Celda() {
        this.entidad = null;
        this.objeto = null;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public boolean isEmpty() {
        return entidad == null && objeto == null;
    }

    public boolean hasEntidad() {
        return entidad != null;
    }

    public boolean hasObjeto() {
        return objeto != null;
    }
}
