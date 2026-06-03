package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;

/**
 * Orquesta las acciones del juego.
 * No contiene lógica propia, delega al Tablero.
 */
public class GameController {
    private final Tablero tablero;

    public GameController(Tablero tablero) {
        this.tablero = tablero;
    }

    public void moverArriba() {
        tablero.mover(0, -1);
    }

    public void moverAbajo() {
        tablero.mover(0, 1);
    }

    public void moverIzquierda() {
        tablero.mover(-1, 0);
    }

    public void moverDerecha() {
        tablero.mover(1, 0);
    }
}
