package org.javafantasticos.sokoban.model.muros;

public class RejaAbierta implements EstadoReja {

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
