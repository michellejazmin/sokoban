package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.interfaces.IEstadoReja;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class Reja extends ElementoBase {
    private IEstadoReja estado;
    public Reja (Coordenada coordenada){
        super(coordenada,'R');
        this.estado = new RejaCerrada();
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
    public IEstadoReja getEstadoReja() {
        return estado;
    }

    @Override
    public void setEstadoReja(IEstadoReja estado) {
        this.estado = estado;
    }
}
