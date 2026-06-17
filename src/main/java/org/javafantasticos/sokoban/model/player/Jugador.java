package org.javafantasticos.sokoban.model.player;

import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

import java.util.ArrayList;
import java.util.List;

public final class Jugador extends ElementoBase {

    public Jugador(Coordenada coordenada) {
        super(coordenada, 'J');
    }

    @Override
    public boolean esMovible() {
        return true;
    }

}