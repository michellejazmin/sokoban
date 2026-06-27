package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.enumerados.Estado;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

// TODO: crear RejaAbierta y RejaCerrada
public final class Reja extends ElementoBase {
    private Estado estado;
    public Reja (Coordenada coordenada){
        super(coordenada,'R');
        this.estado=Estado.CERRADO;

    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Estado getEstado() {
        return estado;
    }

    @Override
    public boolean bloqueaPaso() {
        return estado == Estado.CERRADO;
    }

    @Override
    public boolean esOcupable() {
        return estado == Estado.ABIERTO;
    }

    @Override
    public Estado getEstadoReja() {
        return estado;
    }

    @Override
    public void setEstadoReja(Estado estado) {
        this.estado = estado;
    }
}
