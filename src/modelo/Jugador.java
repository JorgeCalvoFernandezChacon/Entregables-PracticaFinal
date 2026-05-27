package modelo;

public class Jugador extends Entidad {
    private int velocidad;
    private int habitacionId;
    private Habitacion habitacion;
    private Inventario inventario;
    private Equipable[] slots;
    private Llave llaveEquipada;
    private int vidaMaxima;

    public Jugador(int vida, int ataque, int defensa, int velocidad, int fila, int columna) {
        super(vida, ataque, defensa, fila, columna);
        this.vidaMaxima = vida;
        this.velocidad = velocidad;
        this.inventario = new Inventario(10);
        this.slots = new Equipable[Equipable.Slot.values().length];
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getHabitacionId() {
        return habitacionId;
    }

    public void setHabitacionId(int habitacionId) {
        this.habitacionId = habitacionId;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    @Override
    public void setFila(int fila) {
        if (habitacion != null && (fila < 0 || fila >= habitacion.getFilas())) {
            throw new IllegalArgumentException("Fila fuera de los límites de la habitación.");
        }
        super.setFila(fila);
    }

    @Override
    public void setColumna(int columna) {
        if (habitacion != null && (columna < 0 || columna >= habitacion.getColumnas())) {
            throw new IllegalArgumentException("Columna fuera de los límites de la habitación.");
        }
        super.setColumna(columna);
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void recogerObjeto(Objeto obj) {
        inventario.añadirObjeto(obj);
    }

    public Equipable getEquipo(Equipable.Slot slot) {
        return slots[slot.ordinal()];
    }

    public Equipable[] getSlots() {
        return slots;
    }

    public Equipable equipar(Equipable item) {
        int idx = item.getSlot().ordinal();
        Equipable old = slots[idx];
        slots[idx] = item;
        if (inventario.eliminarObjeto(item)) {
            // was in inventory, now removed
        }
        return old;
    }

    public void equiparDirecto(Equipable item) {
        slots[item.getSlot().ordinal()] = item;
    }

    public void desequipar(Equipable.Slot slot) {
        int idx = slot.ordinal();
        if (slots[idx] != null) {
            Celda cell = habitacion.getCelda(fila, columna);
            if (cell != null) cell.setObjeto(slots[idx]);
            slots[idx] = null;
        }
    }

    public Llave getLlaveEquipada() {
        return llaveEquipada;
    }

    public Llave equiparLlave(Llave llave) {
        Llave old = llaveEquipada;
        llaveEquipada = llave;
        inventario.eliminarObjeto(llave);
        return old;
    }

    public void equiparLlaveSilenciosa(Llave llave) {
        llaveEquipada = llave;
    }

    public Llave consumirLlave() {
        Llave l = llaveEquipada;
        llaveEquipada = null;
        return l;
    }

    public void curar(int cantidad) {
        this.vida = Math.min(vidaMaxima, this.vida + cantidad);
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getAtaqueTotal() {
        int bonus = 0;
        for (Equipable e : slots) {
            if (e != null) bonus += e.getBonoAtaque();
        }
        return ataque + bonus;
    }

    public int getDefensaTotal() {
        int bonus = 0;
        for (Equipable e : slots) {
            if (e != null) bonus += e.getBonoDefensa();
        }
        return defensa + bonus;
    }

    @Override
    public String actuar(Habitacion habitacion, Jugador jugador) {
        return null;
    }
}
