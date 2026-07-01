package org.javafantasticos.sokoban.interfaces;

import javax.swing.*;
import java.awt.event.ActionListener;

public interface NavegadorPantallas {
    void agregarPantalla(JPanel pantalla, String nombre);
    void mostrarMenu();
    void mostrarJuego();
    void mostrarGameOver();
    void mostrarVictoria();
    void mostrarPasoNivel();
    void mostrarReplay();
    void setVisible(boolean visible);
}
