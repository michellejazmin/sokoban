package org.javafantasticos.sokoban.interfaces;

public interface ContextoItem {
    void sumarBonus(int monto);
    void terminarPartida(String motivo);
    void sumarUndoExtra();
}
