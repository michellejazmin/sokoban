package org.javafantasticos.sokoban.model.suelo;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public class Destino extends ElementoBase {
    public Destino(Coordenada coordenada) {
        super(coordenada, 'D');
    }
}
