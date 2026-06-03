package org.javafantasticos.sokoban.model.suelo;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public class Suelo extends ElementoBase {
    public Suelo(Coordenada coordenada) {
        super(coordenada, 'S');
    }
}
