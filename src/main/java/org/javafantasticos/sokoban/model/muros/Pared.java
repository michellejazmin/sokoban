package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public final class Pared extends ElementoBase {
    public Pared(Coordenada coordenada) {
        super(coordenada, 'P');
    }

    @Override
    public boolean bloqueaPaso() {
        return true;
    }
}
