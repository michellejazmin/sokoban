package org.javafantasticos.sokoban.interfaces;

public interface HUDDataSource {
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
