package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.model.dto.Coordenada;

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

    public boolean estaVacia() {
        return coordenada == null;
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
}
