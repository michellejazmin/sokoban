package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

// TODO: crear RejaAbierta y RejaCerrada
public final class Reja extends ElementoBase {
    private EstadoReja estado;
    public Reja (Coordenada coordenada){
        super(coordenada,'R');
        this.estado= EstadoReja.CERRADO;

    }

    public void setEstado(EstadoReja estado) {
        this.estado = estado;
    }

    public EstadoReja getEstado() {
        return estado;
    }

    @Override
    public char getSimbolo() {
        return estado == EstadoReja.ABIERTO ? 'S' : 'R';
    }

    @Override
    public boolean bloqueaPaso() {
        return estado == EstadoReja.CERRADO;
    }

    @Override
    public boolean esOcupable() {
        return estado == EstadoReja.ABIERTO;
    }

    @Override
    public EstadoReja getEstadoReja() {
        return estado;
    }

    @Override
    public void setEstadoReja(EstadoReja estado) {
        this.estado = estado;
    }
}
