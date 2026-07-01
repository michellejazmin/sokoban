package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.player.Orientacion;

import java.awt.Component;
import java.util.function.Supplier;

public class InputController {
    private IMovimientos movimientos;
    private Component componente;
    private final Supplier<Tablero> tableroSupplier;
    private final Supplier<Jugador> jugadorSupplier;
    private final Runnable onUndo;

    public InputController(Supplier<Tablero> tableroSupplier, Supplier<Jugador> jugadorSupplier, Runnable onUndo) {
        this.tableroSupplier = tableroSupplier;
        this.jugadorSupplier = jugadorSupplier;
        this.onUndo = onUndo;
    }

    public void registrar(Component componente) {
        if (movimientos != null) {
            movimientos.desregistrarDe(this.componente);
        }
        this.componente = componente;
        this.movimientos = new MovimientoTeclado(this);
        movimientos.registrarEn(componente);
    }

    public void desregistrar(Component componente) {
        if (movimientos != null) movimientos.desregistrarDe(componente);
        this.movimientos = null;
    }

    public void moverArriba() {
        jugadorSupplier.get().setOrientacion(Orientacion.ESPALDA);
        tableroSupplier.get().mover(0, -1);
    }

    public void moverAbajo() {
        jugadorSupplier.get().setOrientacion(Orientacion.FRENTE);
        tableroSupplier.get().mover(0, 1);
    }

    public void moverIzquierda() {
        jugadorSupplier.get().setOrientacion(Orientacion.IZQUIERDA);
        tableroSupplier.get().mover(-1, 0);
    }

    public void moverDerecha() {
        jugadorSupplier.get().setOrientacion(Orientacion.DERECHA);
        tableroSupplier.get().mover(1, 0);
    }

    public void undo() {
        onUndo.run();
    }
}
