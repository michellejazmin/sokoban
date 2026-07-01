package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.EstadoReja;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.interfaces.ContextoItem;

import java.util.function.Consumer;

public abstract class ElementoBase {
    protected Coordenada coordenada;
    private final char simbolo;

    protected ElementoBase(Coordenada coordenada, char simbolo) {
        this.coordenada = coordenada;
        this.simbolo = simbolo;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public char getSimbolo() {
        return simbolo;
    }

    public boolean esOcupable() {
        // Por defecto, ningún elemento es ocupable, es decir, el jugador no se puede ubicar sobre él.
        return false;
    }

    public boolean esMovible() {
        return false;
    }

    public boolean esResbaloso() {
        return false; // por defecto ningún elemento es resbaloso
    }

    public boolean bloqueaPaso() {
        return false; // por defecto ningún elemento bloquea el paso
    }

    public boolean bloqueaPasoAJugadorMasCaja() {
        return false; // por defecto ningún elemento bloquea el paso
    }

    public void actualizar(Coordenada posicionActualJugador, int dx, int dy) {
        return;
    }

    public EstadoReja getEstadoReja() {
        return null;
    }

    public void setEstadoReja(EstadoReja estado) {
    }

    public boolean alSerEmpujada(Consumer<String> onGameOver) {
        return true;
    }

    public ElementoBase aplicar(ContextoItem ctx) {
        return this;
    }

    public String getOverlayTexto() {
        return null;
    }

    public void setTtl(int ttl) {
    }
}
