package org.javafantasticos.sokoban.interfaces;

import org.javafantasticos.sokoban.model.dto.Coordenada;

public interface ElementoTablero {
    Coordenada getCoordenada();

    char getSimbolo();

    boolean esResbaloso();

    boolean bloqueaPaso();
}
