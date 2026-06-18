package org.javafantasticos.sokoban.model.suelo;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public class Cerrojo extends ElementoBase {
    public Cerrojo(Coordenada coordenada) {
        super(coordenada, 'C');
    }

    @Override
    public boolean esOcupable() {
        return true;
    }
}
