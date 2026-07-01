package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class Reja extends ElementoBase {
    private EstadoReja estado;
    public Reja (Coordenada coordenada){
        super(coordenada,'R');
        this.estado = new RejaCerrada(this);
        estado.setContexto(this);
    }

    @Override
    public char getSimbolo() {
        return estado.getSimbolo();
    }

    @Override
    public boolean bloqueaPaso() {
        return estado.bloqueaPaso();
    }

    @Override
    public boolean esOcupable() {
        return estado.esOcupable();
    }

    public boolean estaAbierta() {
        return estado.estaAbierta();
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
