package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.TableroMemento;

import java.util.ArrayList;
import java.util.List;


public class Grabacion {
    private final List<Caretaker.Snapshot> frames;

    public Grabacion() {
        this.frames = new ArrayList<>();
    }

    // Borra la grabación para empezar una partida nueva.
    public void reset() {
        frames.clear();
    }

    // Agrega un frame al final de la grabación.
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
