package org.example.model;

import org.example.interfaces.ElementoTablero;
import org.example.model.dto.Coordenada;

public abstract class ElementoBase implements ElementoTablero {
    private Coordenada coordenada;
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
}
