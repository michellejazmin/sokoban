package org.javafantasticos.sokoban.model.player;

import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class Jugador extends ElementoBase implements IMovimientos {

    public Jugador(Coordenada coordenada) {
        super(coordenada, 'J');
    }

    @Override
    public void arriba() {
        desplazar(0, -1);
    }

    @Override
    public void abajo() {
        desplazar(0, 1);
    }

    @Override
    public void derecha() {
        desplazar(1, 0);
    }

    @Override
    public void izquierda() {
        desplazar(-1, 0);
    }

    // El jugador mismo gestiona su movimiento...
    private void desplazar(int movimientoX, int movimientoY) {
        Coordenada coord = this.getCoordenada();
        if (coord != null) {
            coord.setPosX(coord.getPosX() + movimientoX);
            coord.setPosY(coord.getPosY() + movimientoY);
        }
    }
}