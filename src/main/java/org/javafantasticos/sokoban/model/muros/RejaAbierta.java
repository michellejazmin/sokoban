package org.javafantasticos.sokoban.model.muros;

public class RejaAbierta extends EstadoReja {

    public RejaAbierta(Reja contexto) {
        super(contexto);
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

}
