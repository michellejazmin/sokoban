package org.javafantasticos.sokoban.interfaces;

import javax.swing.*;

public interface INavegadorPantallas {
    void agregarPantalla(JPanel pantalla, String nombre);
    void mostrarMenu();
    void mostrarJuego();
    void mostrarGameOver();
    void mostrarVictoria();
    void mostrarPasoNivel();
    void mostrarReplay();
    void setVisible(boolean visible);
}
