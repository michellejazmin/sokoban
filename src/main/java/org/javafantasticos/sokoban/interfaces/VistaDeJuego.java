package org.javafantasticos.sokoban.interfaces;

public interface VistaDeJuego {
    Suscriptor getTableroPanel();
    VistaHUD getHudPanel();
    void recuperarTablero();
    boolean requestFocusInWindow();
}
