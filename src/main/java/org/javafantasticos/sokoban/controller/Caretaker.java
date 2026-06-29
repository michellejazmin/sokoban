package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.TableroMemento;

import java.util.ArrayDeque;
import java.util.Deque;

public class Caretaker { ;
    private static final int UNDO_STEPS = 5;
    private static final int MAX_UNDO_USES = 3;
    private static final int MAX_HISTORY = UNDO_STEPS * MAX_UNDO_USES + 1;

    private final Deque<Snapshot> history;
    private int undoCount;

    public record Snapshot(TableroMemento memento, int steps, int pushes) {}

    public Caretaker() {
        this.history = new ArrayDeque<>();
        this.undoCount = 0;
    }

    public void saveState(TableroMemento memento, int steps, int pushes) {
        history.addLast(new Snapshot(memento, steps, pushes));

        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
    }

    public boolean canUndo() {
        return undoCount < MAX_UNDO_USES && history.size() > UNDO_STEPS;
    }

    /**
     * @return el Snapshot restaurado, o null si no se puede deshacer
     */
    public Snapshot undo(Tablero tablero) {
        if (!canUndo()) {
            return null;
        }

        for (int i = 0; i < UNDO_STEPS; i++) {
            history.removeLast();
        }

        undoCount++;
        Snapshot snap = history.peekLast();
        tablero.restaurar(snap.memento());
        return snap;
    }

    public void reset() {
        history.clear();
        undoCount = 0;
    }

    public int getRemainingUndos() {
        return MAX_UNDO_USES - undoCount;
    }

    public int getUndoStepSize() {
        return UNDO_STEPS;
    }

    public int getMaxUndoUses() {
        return MAX_UNDO_USES;
    }
}
