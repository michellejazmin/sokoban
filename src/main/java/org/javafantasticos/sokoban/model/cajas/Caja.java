package org.javafantasticos.sokoban.model.cajas;

import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.ElementoBase;

public abstract class Caja extends ElementoBase {
    protected Caja(Coordenada coordenada, char simbolo) {
        super(coordenada, simbolo);
    }

    @Override
    public void actualizar(Coordenada posicionActualJugador, int dx, int dy) {
        int posXFinalJugador = posicionActualJugador.getPosX() + dx;
        int posYFinalJugador = posicionActualJugador.getPosY() + dy;

        if (posXFinalJugador == this.coordenada.getPosX() && posYFinalJugador == this.coordenada.getPosY())
        {
            this.coordenada.setPosX(coordenada.getPosX() + dx);
            this.coordenada.setPosY(coordenada.getPosY() + dy);
        }
    }
}
