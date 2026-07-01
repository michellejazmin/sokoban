package org.javafantasticos.sokoban.interfaces;

public interface IContextoItem {
    void sumarBonus(int monto);
    void terminarPartida(String motivo);
    void sumarUndoExtra();
}
