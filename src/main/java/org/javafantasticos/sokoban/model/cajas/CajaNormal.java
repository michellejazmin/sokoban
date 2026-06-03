package org.javafantasticos.sokoban.model.cajas;

import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class CajaNormal extends Caja {
    public CajaNormal(Coordenada coordenada) {
        super(coordenada, 'N');
    }
}
