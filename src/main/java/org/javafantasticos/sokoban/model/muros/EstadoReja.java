package org.javafantasticos.sokoban.model.muros;

public interface EstadoReja {
    char getSimbolo();
    boolean bloqueaPaso();
    boolean esOcupable();
    boolean estaAbierta();
}
