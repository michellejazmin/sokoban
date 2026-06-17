package org.javafantasticos.sokoban.model.player;

import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;

import java.util.ArrayList;
import java.util.List;

public final class Jugador extends ElementoBase implements IMovimientos {

    //private final List<Suscriptor> suscriptores;

    public Jugador(Coordenada coordenada) {
        super(coordenada, 'J');
        //this.suscriptores = new ArrayList<>();
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

    /*public void suscribir(Suscriptor suscriptor) {
        this.suscriptores.add(suscriptor);
    }

    public void desuscribir(Suscriptor suscriptor) {
        this.suscriptores.remove(suscriptor);
    }

    public void notificarSuscriptores() {
        suscriptores.forEach((suscriptor) -> {
            suscriptor.actualizar(this.coordenada, , );
        });
    }*/

}