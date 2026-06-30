package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.TableroMemento;

import java.util.ArrayList;
import java.util.List;

/**
 * Graba el 100% de los estados de la partida reutilizando el Memento del Tablero.
 *
 * A diferencia del {@link Caretaker} (que está acotado para el undo y descarta el
 * historial más viejo), esta grabación no descarta nada: conserva cada frame en
 * orden para poder reproducir la partida completa de principio a fin.
 *
 * Reutiliza el record {@link Caretaker.Snapshot} para que cada frame lleve, además
 * del memento, el puntaje de pasos/empujes en ese instante.
 */
public class Grabacion {
    private final List<Caretaker.Snapshot> frames;

    public Grabacion() {
        this.frames = new ArrayList<>();
    }

    /** Borra la grabación para empezar una partida nueva. */
    public void reset() {
        frames.clear();
    }

    /** Agrega un frame al final de la grabación. */
    public void grabar(TableroMemento memento, int steps, int pushes) {
        frames.add(new Caretaker.Snapshot(memento, steps, pushes));
    }

    public boolean isEmpty() {
        return frames.isEmpty();
    }

    public int size() {
        return frames.size();
    }

    public Caretaker.Snapshot get(int indice) {
        return frames.get(indice);
    }
}
