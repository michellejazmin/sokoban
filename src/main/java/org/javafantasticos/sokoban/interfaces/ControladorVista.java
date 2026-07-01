package org.javafantasticos.sokoban.interfaces;

import org.javafantasticos.sokoban.model.player.Orientacion;

public interface ControladorVista {
    void undo();
    void reiniciarNivel();
    void volverAlMenu();
    Orientacion getOrientacion();
    void setOnMove(Runnable callback);
    int getScore();
    int getNivelActual();
    int getTotalNiveles();
    int getSteps();
    int getPushes();
    int getCajasEnDestino();
    int getTotalCajas();
    int getUndoStepSize();
    int getUndoRemaining();
    int getMaxUndoUses();
    boolean canUndo();
}
