package org.javafantasticos.sokoban.model.muros;

public abstract class EstadoReja {
    private Reja contexto;

    public EstadoReja(Reja contexto) {
        this.contexto = contexto;
    }

    public void setContexto(Reja reja) {
        this.contexto = reja;
    }

    abstract char getSimbolo();
    abstract boolean bloqueaPaso();
    abstract boolean esOcupable();
    abstract boolean estaAbierta();
}
