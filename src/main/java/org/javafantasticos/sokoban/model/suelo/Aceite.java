package org.javafantasticos.sokoban.model.suelo;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public class Aceite extends ElementoBase {
    public Aceite(Coordenada coordenada) {
        super(coordenada, 'A');
    }

    @Override
    public boolean esOcupable() {
        return true;
    }

    @Override
    public boolean esResbaloso() {
        return true;
    }
}
