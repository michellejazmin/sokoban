package org.javafantasticos.sokoban.interfaces;

public interface EstadoReja {
    char getSimbolo();
    boolean bloqueaPaso();
    boolean esOcupable();
    boolean estaAbierta();
}
