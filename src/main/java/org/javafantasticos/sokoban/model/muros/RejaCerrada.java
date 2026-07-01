package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.interfaces.EstadoReja;

public class RejaCerrada implements EstadoReja {

    public RejaCerrada() {
        // Constructor vacío, no se necesita hacer nada con el contexto en este caso
    }

    @Override
    public char getSimbolo() {
        return 'R';
    }

    @Override
    public boolean bloqueaPaso() {
        return true;
    }

    @Override
    public boolean esOcupable() {
        return false;
    }

    @Override
    public boolean estaAbierta() {
        return false;
    }

}
