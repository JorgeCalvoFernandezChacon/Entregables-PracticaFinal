package logica;

import modelo.*;
import estructuras.*;
import java.io.*;

public class PersistenciaService {

    public static class DatosPartida {
        public Jugador jugador;
        public int turnosRestantes;
        public int exitHabitacionId;
        public Habitacion[] habitaciones;
        public Grafo grafo;
        public String dificultad;
    }

    public static void guardarPartida(String archivo, Jugador jugador, GestorTurnos gestorTurnos,
        Habitacion[] habitaciones, Grafo grafo, int exitHabitacionId, String dificultad) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"turnosRestantes\": ").append(gestorTurnos.getTurnosRestantes()).append(",\n");
        sb.append("  \"exitHabitacionId\": ").append(exitHabitacionId).append(",\n");
        sb.append("  \"dificultad\": \"").append(escape(dificultad)).append("\",\n");
        sb.append("  \"jugador\": ").append(serializarJugador(jugador)).append(",\n");
        sb.append("  \"habitaciones\": [\n");
        for (int i = 0; i < habitaciones.length; i++) {
            if (i > 0) sb.append(",\n");
            sb.append(serializarHabitacion(habitaciones[i], i));
        }
        sb.append("\n  ],\n");
        sb.append("  \"grafo\": ").append(serializarGrafo(grafo)).append("\n");
        sb.append("}\n");
        try (FileWriter fw = new FileWriter(archivo)) {
            fw.write(sb.toString());
        }
    }

    public static DatosPartida cargarPartida(String archivo) throws IOException {
        String content;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            content = sb.toString();
        }

        DatosPartida datos = new DatosPartida();
        datos.turnosRestantes = intVal(content, "turnosRestantes");
        datos.exitHabitacionId = intVal(content, "exitHabitacionId");
        datos.dificultad = strVal(content, "dificultad");

        String jugJson = objVal(content, "jugador");
        int vidaMax = intVal(jugJson, "vidaMaxima");
        if (vidaMax == 0) vidaMax = intVal(jugJson, "vida");
        int vida = intVal(jugJson, "vida");
        int ataque = intVal(jugJson, "ataque");
        int defensa = intVal(jugJson, "defensa");
        int velocidad = intVal(jugJson, "velocidad");
        int fila = intVal(jugJson, "fila");
        int columna = intVal(jugJson, "columna");
        int habId = intVal(jugJson, "habitacionId");

        datos.jugador = new Jugador(vidaMax, ataque, defensa, velocidad, fila, columna);
        datos.jugador.setVida(vida);
        datos.jugador.setHabitacionId(habId);

        String invArr = arrVal(jugJson, "inventario");
        if (!invArr.equals("[]")) {
            String[] items = splitArray(invArr);
            for (String item : items) {
                Objeto obj = parseObjeto(item);
                if (obj != null) datos.jugador.getInventario().añadirObjeto(obj);
            }
        }

        String eqArr = arrVal(jugJson, "equipo");
        if (eqArr != null && !eqArr.equals("[]")) {
            String[] items = splitArray(eqArr);
            for (String item : items) {
                Objeto eq = parseObjeto(item);
                if (eq instanceof Equipable) {
                    datos.jugador.equiparDirecto((Equipable) eq);
                }
            }
        }

        String llaveStr = objVal(jugJson, "llaveEquipada");
        if (llaveStr != null && !llaveStr.isEmpty() && !llaveStr.equals("null")) {
            Objeto llaveObj = parseObjeto(llaveStr);
            if (llaveObj instanceof Llave) {
                datos.jugador.equiparLlaveSilenciosa((Llave) llaveObj);
            }
        }

        String habsArr = arrVal(content, "habitaciones");
        String[] habItems = splitArray(habsArr);
        datos.habitaciones = new Habitacion[habItems.length];
        for (int i = 0; i < habItems.length; i++) {
            datos.habitaciones[i] = parseHabitacion(habItems[i]);
        }

        if (habId < 0 || habId >= datos.habitaciones.length) {
            habId = 0;
        }
        Celda playerCell = datos.habitaciones[habId]
            .getCelda(datos.jugador.getFila(), datos.jugador.getColumna());
        if (playerCell != null) playerCell.setEntidad(datos.jugador);

        String grafoStr = objVal(content, "grafo");
        datos.grafo = parseGrafo(grafoStr);

        return datos;
    }

    public static boolean archivoExiste(String archivo) {
        return new File(archivo).exists();
    }

    // --- Serialización manual ---

    private static String serializarJugador(Jugador j) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"vida\": ").append(j.getVida()).append(",\n");
        sb.append("    \"vidaMaxima\": ").append(j.getVidaMaxima()).append(",\n");
        sb.append("    \"ataque\": ").append(j.getAtaque()).append(",\n");
        sb.append("    \"defensa\": ").append(j.getDefensa()).append(",\n");
        sb.append("    \"velocidad\": ").append(j.getVelocidad()).append(",\n");
        sb.append("    \"habitacionId\": ").append(j.getHabitacionId()).append(",\n");
        sb.append("    \"fila\": ").append(j.getFila()).append(",\n");
        sb.append("    \"columna\": ").append(j.getColumna()).append(",\n");
        sb.append("    \"inventario\": [");
        Inventario inv = j.getInventario();
        for (int i = 0; i < inv.getTamaño(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\n      ").append(serializarObjeto(inv.obtenerObjeto(i)));
        }
        sb.append(inv.getTamaño() > 0 ? "\n    " : "").append("],\n");
        sb.append("    \"equipo\": [");
        Equipable[] slots = j.getSlots();
        int written = 0;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] != null) {
                if (written > 0) sb.append(",");
                sb.append("\n      ").append(serializarObjeto(slots[i]));
                written++;
            }
        }
        sb.append(written > 0 ? "\n    " : "").append("],\n");
        sb.append("    \"llaveEquipada\": ").append(j.getLlaveEquipada() != null ? serializarObjeto(j.getLlaveEquipada()) : "null").append("\n");
        sb.append("  }");
        return sb.toString();
    }

    private static String serializarObjeto(Objeto obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"nombre\": \"").append(escape(obj.getNombre())).append("\"");
        sb.append(", \"descripcion\": \"").append(escape(obj.getDescripcion())).append("\"");
        if (obj instanceof Equipable e) {
            sb.append(", \"tipo\": \"Equipable\"");
            sb.append(", \"slot\": \"").append(e.getSlot().name()).append("\"");
            sb.append(", \"bonoAtaque\": ").append(e.getBonoAtaque());
            sb.append(", \"bonoDefensa\": ").append(e.getBonoDefensa());
        } else if (obj instanceof Consumible c) {
            sb.append(", \"tipo\": \"Consumible\"");
            sb.append(", \"recuperacionVida\": ").append(c.getRecuperacionVida());
        } else if (obj instanceof Llave l) {
            sb.append(", \"tipo\": \"Llave\"");
            sb.append(", \"puertaId\": \"").append(escape(l.getPuertaId())).append("\"");
        } else if (obj instanceof Trampa t) {
            sb.append(", \"tipo\": \"Trampa\"");
            sb.append(", \"daño\": ").append(t.getDaño());
        } else if (obj instanceof Puerta p) {
            sb.append(", \"tipo\": \"Puerta\"");
            sb.append(", \"llaveId\": ").append(p.getRequiredKeyId() != null ? "\"" + escape(p.getRequiredKeyId()) + "\"" : "null");
            sb.append(", \"habitacionDestinoId\": ").append(p.getHabitacionDestinoId());
            sb.append(", \"abierta\": ").append(p.isAbierta());
        }
        sb.append("}");
        return sb.toString();
    }

    private static String serializarEntidad(Entidad e) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"tipo\": \"").append(e instanceof Enemigo ? "Enemigo" : "Jugador").append("\"");
        sb.append(", \"vida\": ").append(e.getVida());
        sb.append(", \"ataque\": ").append(e.getAtaque());
        sb.append(", \"defensa\": ").append(e.getDefensa());
        sb.append(", \"fila\": ").append(e.getFila());
        sb.append(", \"columna\": ").append(e.getColumna());
        sb.append("}");
        return sb.toString();
    }

    private static String serializarHabitacion(Habitacion h, int indice) {
        StringBuilder sb = new StringBuilder();
        sb.append("    {\n");
        sb.append("      \"indice\": ").append(indice).append(",\n");
        sb.append("      \"filas\": ").append(h.getFilas()).append(",\n");
        sb.append("      \"columnas\": ").append(h.getColumnas()).append(",\n");
        sb.append("      \"celdas\": [\n");
        boolean first = true;
        for (int r = 0; r < h.getFilas(); r++) {
            for (int c = 0; c < h.getColumnas(); c++) {
                if (!first) sb.append(",\n");
                first = false;
                sb.append("        { \"f\": ").append(r).append(", \"c\": ").append(c);
                Celda celda = h.getCelda(r, c);
                sb.append(", \"entidad\": ").append(celda.hasEntidad() ? serializarEntidad(celda.getEntidad()) : "null");
                sb.append(", \"objeto\": ").append(celda.hasObjeto() ? serializarObjeto(celda.getObjeto()) : "null");
                sb.append(" }");
            }
        }
        sb.append("\n      ]\n");
        sb.append("    }");
        return sb.toString();
    }

    private static String serializarGrafo(Grafo g) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"numVertices\": ").append(g.getNumVertices()).append(",\n");
        sb.append("    \"adyacencia\": [\n");
        for (int i = 0; i < g.getNumVertices(); i++) {
            if (i > 0) sb.append(",\n");
            sb.append("      [");
            ListaEnlazada<Integer> neighbors = g.getAdjList()[i];
            for (int j = 0; j < neighbors.size(); j++) {
                if (j > 0) sb.append(",");
                sb.append(neighbors.get(j));
            }
            sb.append("]");
        }
        sb.append("\n    ]\n");
        sb.append("  }");
        return sb.toString();
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // --- Parseo manual simple ---

    private static int intVal(String json, String key) {
        try { return Integer.parseInt(strVal(json, key)); }
        catch (NumberFormatException e) { return 0; }
    }

    private static int intVal(String json, String key, int defaultValue) {
        String s = strVal(json, key);
        if (s.isEmpty()) return defaultValue;
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    private static String strVal(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx < 0) return "";
        idx += search.length();
        while (idx < json.length() && json.charAt(idx) == ' ') idx++;
        if (idx >= json.length()) return "";
        char c = json.charAt(idx);
        if (c == '"') {
            StringBuilder val = new StringBuilder();
            idx++;
            while (idx < json.length()) {
                c = json.charAt(idx);
                if (c == '\\') {
                    idx++;
                    if (idx < json.length()) val.append(json.charAt(idx));
                } else if (c == '"') {
                    break;
                } else {
                    val.append(c);
                }
                idx++;
            }
            return val.toString();
        } else if (c == 'n') {
            return "null";
        } else {
            StringBuilder val = new StringBuilder();
            while (idx < json.length()) {
                c = json.charAt(idx);
                if (c == ',' || c == '}' || c == ']' || c == ' ' || c == '\n' || c == '\r') break;
                val.append(c);
                idx++;
            }
            return val.toString();
        }
    }

    private static String objVal(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx < 0) return "";
        idx += search.length();
        while (idx < json.length() && json.charAt(idx) == ' ') idx++;
        if (idx >= json.length() || json.charAt(idx) != '{') {
            if (idx < json.length() && json.charAt(idx) == 'n') return "null";
            return "";
        }
        int depth = 0;
        int start = idx;
        while (idx < json.length()) {
            char c = json.charAt(idx);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return json.substring(start, idx + 1);
            } else if (c == '"') {
                idx++;
                while (idx < json.length()) {
                    if (json.charAt(idx) == '\\') idx++;
                    else if (json.charAt(idx) == '"') break;
                    idx++;
                }
            }
            idx++;
        }
        return "";
    }

    private static String arrVal(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx < 0) return "[]";
        idx += search.length();
        while (idx < json.length() && json.charAt(idx) == ' ') idx++;
        if (idx >= json.length() || json.charAt(idx) != '[') return "[]";
        int depth = 0;
        int start = idx;
        while (idx < json.length()) {
            char c = json.charAt(idx);
            if (c == '[') depth++;
            else if (c == ']') {
                depth--;
                if (depth == 0) return json.substring(start, idx + 1);
            } else if (c == '"') {
                idx++;
                while (idx < json.length()) {
                    if (json.charAt(idx) == '\\') idx++;
                    else if (json.charAt(idx) == '"') break;
                    idx++;
                }
            }
            idx++;
        }
        return "[]";
    }

    private static String[] splitArray(String arr) {
        if (arr.length() < 2) return new String[0];
        String inner = arr.substring(1, arr.length() - 1).trim();
        if (inner.isEmpty()) return new String[0];
        java.util.List<String> items = new java.util.ArrayList<>();
        int depth = 0;
        int start = 0;
        boolean inStr = false;
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '"' && (i == 0 || inner.charAt(i - 1) != '\\')) {
                inStr = !inStr;
            }
            if (!inStr) {
                if (c == '{' || c == '[') depth++;
                else if (c == '}' || c == ']') depth--;
                else if (c == ',' && depth == 0) {
                    items.add(inner.substring(start, i).trim());
                    start = i + 1;
                }
            }
        }
        String last = inner.substring(start).trim();
        if (!last.isEmpty()) items.add(last);
        return items.toArray(new String[0]);
    }

    private static Objeto parseObjeto(String json) {
        String tipo = strVal(json, "tipo");
        String nombre = strVal(json, "nombre");
        String desc = strVal(json, "descripcion");
        if (tipo == null || tipo.isEmpty() || nombre == null || nombre.isEmpty()) return null;
        return switch (tipo) {
            case "Equipable" -> {
                String slotName = strVal(json, "slot");
                Equipable.Slot slot;
                try { slot = Equipable.Slot.valueOf(slotName); }
                catch (Exception e) { slot = Equipable.Slot.MANO; }
                yield new Equipable(nombre, desc, intVal(json, "bonoAtaque"), intVal(json, "bonoDefensa"), slot);
            }
            case "Consumible" -> new Consumible(nombre, desc, intVal(json, "recuperacionVida"));
            case "Llave" -> new Llave(nombre, desc, strVal(json, "puertaId"));
            case "Trampa" -> new Trampa(nombre, desc, intVal(json, "daño"));
            case "Puerta" -> {
                String llaveId = strVal(json, "llaveId");
                Puerta p = new Puerta(nombre, desc,
                    "null".equals(llaveId) ? null : llaveId,
                    intVal(json, "habitacionDestinoId"));
                String abierta = strVal(json, "abierta");
                if ("true".equals(abierta)) p.abrir();
                yield p;
            }
            default -> null;
        };
    }

    private static Habitacion parseHabitacion(String json) {
        int filas = intVal(json, "filas");
        int columnas = intVal(json, "columnas");
        Habitacion h = new Habitacion(filas, columnas);
        String celdasArr = arrVal(json, "celdas");
        String[] items = splitArray(celdasArr);
        for (String item : items) {
            int r = intVal(item, "f");
            int c = intVal(item, "c");
            Celda celda = h.getCelda(r, c);
            String entStr = objVal(item, "entidad");
            if (entStr != null && !entStr.isEmpty() && !entStr.equals("null")) {
                String tipoEnt = strVal(entStr, "tipo");
                if ("Enemigo".equals(tipoEnt)) {
                    Entidad e = new Enemigo(
                        intVal(entStr, "vida"), intVal(entStr, "ataque"),
                        intVal(entStr, "defensa"), intVal(entStr, "fila"), intVal(entStr, "columna"),
                        intVal(entStr, "velocidad", 3));
                    celda.setEntidad(e);
                }
            }
            String objStr = objVal(item, "objeto");
            if (objStr != null && !objStr.isEmpty() && !objStr.equals("null")) {
                Objeto obj = parseObjeto(objStr);
                if (obj != null) celda.setObjeto(obj);
            }
        }
        return h;
    }

    private static Grafo parseGrafo(String json) {
        int n = intVal(json, "numVertices");
        Grafo g = new Grafo(n);
        String adjArr = arrVal(json, "adyacencia");
        String[] rows = splitArray(adjArr);
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            String inner = row.substring(1, row.length() - 1);
            if (inner.isEmpty()) continue;
            String[] parts = inner.split(",");
            for (String part : parts) {
                int dest = Integer.parseInt(part.trim());
                if (dest > i) g.addEdge(i, dest, true);
            }
        }
        return g;
    }
}
