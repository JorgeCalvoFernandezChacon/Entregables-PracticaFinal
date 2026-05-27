package logica;

import modelo.Entidad;
import modelo.Jugador;

public class CombateService {

    public int calcularDanio(Entidad atacante, Entidad defensor) {
        double factor = Math.random() * 2;
        int ataque = (atacante instanceof Jugador) ? ((Jugador) atacante).getAtaqueTotal() : atacante.getAtaque();
        int danio = (int) Math.max(0, ataque * factor - defensor.getDefensa());
        return danio;
    }

    public int ejecutarAtaque(Entidad atacante, Entidad defensor) {
        if (atacante.isMuerto() || defensor.isMuerto()) {
            return 0;
        }
        int danio = calcularDanio(atacante, defensor);
        defensor.setVida(defensor.getVida() - danio);
        String atacanteEmoji = (atacante instanceof Jugador) ? "🧙" : "👹";
        String defensorEmoji = (defensor instanceof Jugador) ? "🧙" : "👹";
        SistemaLog.getInstance().registrarEvento(atacanteEmoji + " atacó a " + defensorEmoji + " causando " + danio + " de daño.");
        if (defensor.getVida() <= 0) {
            defensor.setMuerto(true);
            SistemaLog.getInstance().registrarEvento("💀 " + defensorEmoji + " ha sido derrotado.");
        }
        return danio;
    }
}
