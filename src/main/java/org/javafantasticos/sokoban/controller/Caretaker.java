package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.TableroMemento;

import java.util.ArrayDeque;
import java.util.Deque;

public class Caretaker {
    private static final int MAX_HISTORY = 15;
    private static final int UNDO_STEPS = 5;
    private static final int MAX_UNDO_USES = 3;

    private final Deque<TableroMemento> history;
    private int undoCount;

    public Caretaker() {
        this.history = new ArrayDeque<>();
        this.undoCount = 0;
    }

    public void saveState(TableroMemento memento) {
        history.addLast(memento);

        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
    }

    public boolean canUndo() {
        return undoCount < MAX_UNDO_USES && history.size() > UNDO_STEPS;
    }

    public void undo(Tablero tablero) {
        if (!canUndo()) {
            return;
        }

        for (int i = 0; i < UNDO_STEPS; i++) {
            history.removeLast();
        }

        undoCount++;
        tablero.restaurar(history.peekLast());
    }
}
