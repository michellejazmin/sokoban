package org.javafantasticos.sokoban.interfaces;

import org.javafantasticos.sokoban.model.dto.Coordenada;

// Cumple el rol del Suscriptor en el patrón Observer
public interface ElementoTablero {
    Coordenada getCoordenada();

    char getSimbolo();

    boolean esResbaloso();

    boolean bloqueaPaso();

    // Este método aplica el patrón Observer
    public void actualizar(Coordenada posicionJugador, int dx, int dy);
}
