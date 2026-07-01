package org.javafantasticos.sokoban.model.player;


import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class Jugador extends ElementoBase {
    private Orientacion orientacion;

    public Jugador(Coordenada coordenada) {
        super(coordenada, 'J');
        this.orientacion = Orientacion.FRENTE;
    }

    @Override
    public boolean esMovible() {
        return true;
    }

    public void setOrientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
    }

    public Orientacion getOrientacion() {
        return this.orientacion;
    }

}