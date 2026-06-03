package org.javafantasticos.sokoban.model.cajas;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public abstract class Caja extends ElementoBase {
    protected Caja(Coordenada coordenada, char simbolo) {
        super(coordenada, simbolo);
    }
}
