package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.ElementoTablero;
import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public abstract class ElementoBase implements ElementoTablero {//, Suscriptor {
    protected Coordenada coordenada;
    private final char simbolo;

    protected ElementoBase(Coordenada coordenada, char simbolo) {
        this.coordenada = coordenada;
        this.simbolo = simbolo;
    }

    @Override
    public Coordenada getCoordenada() {
        return coordenada;
    }

    @Override
    public char getSimbolo() {
        return simbolo;
    }

    @Override
    public boolean esResbaloso() {
        return false; // por defecto ningún elemento es resbaloso
    }

    @Override
    public boolean bloqueaPaso() {
        return false; // por defecto ningún elemento bloquea el paso
    }

    // TODO: arreglar esta porquería
    @Override
    public void actualizar(Coordenada posicionActualJugador, int dx, int dy) {
        return;
    }
}
