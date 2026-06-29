package org.javafantasticos.sokoban.model.cajas;

import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

import java.util.function.Consumer;

public abstract class Caja extends ElementoBase {
    protected Caja(Coordenada coordenada, char simbolo) {
        super(coordenada, simbolo);
    }

    public int getTtl() {
        return 0;
    }

    public void setTtl(int ttl) {
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

    @Override
    public boolean esMovible() {
        return true;
    }

    @Override
    public boolean bloqueaPasoAJugadorMasCaja() {
        return true;
    }

    public boolean esCajaLlave() {
        return false;
    }

    public boolean alSerEmpujada(Consumer<String> onGameOver) {
        return true;
    }
}
