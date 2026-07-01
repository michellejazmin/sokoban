package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.TableroMemento;

public class GestorDePartida {
    private static final int SCORE_PER_LEVEL = 1000;
    private static final int STEP_PENALTY = 10;
    private static final int PUSH_PENALTY = 15;

    private final Caretaker caretaker;
    private final Grabacion grabacion;

    private int steps;
    private int pushes;
    private int bonus;
    private boolean partidaTerminada;

    public GestorDePartida() {
        this.caretaker = new Caretaker();
        this.grabacion = new Grabacion();
        this.steps = 0;
        this.pushes = 0;
        this.bonus = 0;
        this.partidaTerminada = false;
    }

    public void guardarInicial(Tablero tablero) {
        TableroMemento memento = tablero.crearMemento();
        caretaker.saveState(memento, 0, 0);
        grabacion.grabar(memento, 0, 0);
    }

    public void registrarMovimiento(TableroMemento memento, int pushCount) {
        steps++;
        pushes += pushCount;
        caretaker.saveState(memento, steps, pushes);
        grabacion.grabar(memento, steps, pushes);
    }

    public boolean undo(Tablero tablero) {
        Caretaker.Snapshot snap = caretaker.undo(tablero);
        if (snap != null) {
            steps = snap.steps();
            pushes = snap.pushes();
            return true;
        }
        return false;
    }

    public void reiniciar() {
        caretaker.reset();
        grabacion.reset();
        steps = 0;
        pushes = 0;
        bonus = 0;
        partidaTerminada = false;
    }

    public int getSteps() {
        return steps;
    }

    public int getPushes() {
        return pushes;
    }

    public int getScore() {
        return Math.max(0, SCORE_PER_LEVEL + bonus - steps * STEP_PENALTY - pushes * PUSH_PENALTY);
    }

    public boolean canUndo() {
        return caretaker.canUndo();
    }

    public int getUndoRemaining() {
        return caretaker.getRemainingUndos();
    }

    public int getUndoStepSize() {
        return caretaker.getUndoStepSize();
    }

    public int getMaxUndoUses() {
        return caretaker.getMaxUndoUses();
    }

    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    public void setPartidaTerminada(boolean partidaTerminada) {
        this.partidaTerminada = partidaTerminada;
    }

    public void sumarBonus(int monto) {
        bonus += monto;
    }

    public void terminarPartida() {
        partidaTerminada = true;
    }

    public void sumarUndoExtra() {
        caretaker.agregarUsoUndo();
    }

    public boolean isGrabacionVacia() {
        return grabacion.isEmpty();
    }

    public Grabacion getGrabacion() {
        return grabacion;
    }
}
