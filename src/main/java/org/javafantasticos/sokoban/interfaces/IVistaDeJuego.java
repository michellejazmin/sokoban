package org.javafantasticos.sokoban.interfaces;

public interface IVistaDeJuego {
    ISuscriptor getTableroPanel();
    IVistaHUD getHudPanel();
    void recuperarTablero();
    boolean requestFocusInWindow();
}
