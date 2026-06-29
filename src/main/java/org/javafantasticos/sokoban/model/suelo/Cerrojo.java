package org.javafantasticos.sokoban.model.suelo;

import org.javafantasticos.sokoban.model.dto.Coordenada;

public class Cerrojo extends Destino {
    public Cerrojo(Coordenada coordenada) {
        super(coordenada, 'C');
    }

    @Override
    public boolean esCerrojo() {
        return true;
    }
}
