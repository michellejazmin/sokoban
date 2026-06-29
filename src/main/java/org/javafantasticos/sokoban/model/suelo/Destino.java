package org.javafantasticos.sokoban.model.suelo;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public class Destino extends ElementoBase {
    public Destino(Coordenada coordenada) {
        super(coordenada, 'D');
    }

    protected Destino(Coordenada coordenada, char simbolo) {
        super(coordenada, simbolo);
    }

    @Override
    public boolean esOcupable() {
        return true;
    }

    public boolean esCerrojo() {
        return false;
    }
}
