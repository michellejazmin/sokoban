package org.javafantasticos.sokoban.interfaces;

public interface IEstadoReja {
    char getSimbolo();
    boolean bloqueaPaso();
    boolean esOcupable();
    boolean estaAbierta();
}
