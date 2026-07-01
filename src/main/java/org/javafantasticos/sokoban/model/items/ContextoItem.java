package org.javafantasticos.sokoban.model.items;

public interface ContextoItem {
    void sumarBonus(int monto);
    void terminarPartida(String motivo);
    void sumarUndoExtra();
}
