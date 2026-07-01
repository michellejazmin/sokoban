package org.javafantasticos.sokoban.model.muros;

public class RejaCerrada extends EstadoReja {

    public RejaCerrada(Reja contexto) {
        super(contexto);
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

}
