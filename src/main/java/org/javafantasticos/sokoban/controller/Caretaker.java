package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.TableroMemento;

import java.util.ArrayDeque;
import java.util.Deque;

public class Caretaker { ;
    private static final int UNDO_STEPS = 5;
    private static final int MAX_UNDO_USES_INICIAL = 3;

    private final Deque<Snapshot> history;
    private int undoCount;
    private int maxUndoUses;

    public record Snapshot(TableroMemento memento, int steps, int pushes) {}

    public Caretaker() {
        this.history = new ArrayDeque<>();
        this.undoCount = 0;
        this.maxUndoUses = MAX_UNDO_USES_INICIAL;
    }

    public void saveState(TableroMemento memento, int steps, int pushes) {
        history.addLast(new Snapshot(memento, steps, pushes));

        if (history.size() > UNDO_STEPS * maxUndoUses + 1) {
            history.removeFirst();
        }
    }

    public boolean canUndo() {
        return undoCount < maxUndoUses && history.size() > UNDO_STEPS;
    }

    public void agregarUsoUndo() {
        maxUndoUses++;
    }

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
        maxUndoUses = MAX_UNDO_USES_INICIAL;
    }

    public int getRemainingUndos() {
        return maxUndoUses - undoCount;
    }

    public int getUndoStepSize() {
        return UNDO_STEPS;
    }

    public int getMaxUndoUses() {
        return maxUndoUses;
    }
}
