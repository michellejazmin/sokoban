package org.javafantasticos.sokoban.model.muros;

import org.javafantasticos.sokoban.interfaces.IEstadoReja;

public class RejaAbierta implements IEstadoReja {

    public RejaAbierta() {
        // Constructor vacío, no se necesita hacer nada con el contexto en este caso
    }

    @Override
    public char getSimbolo() {
        return 'S';
    }

    @Override
    public boolean bloqueaPaso() {
        return false;
    }

    @Override
    public boolean esOcupable() {
        return true;
    }

    @Override
    public boolean estaAbierta() {
        return true;
    }

}
