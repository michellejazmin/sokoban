package org.javafantasticos.sokoban.interfaces;

import java.awt.event.ActionListener;

public interface IPantallaVictoria {
    void setMensaje(String mensaje);
    void escucharBotonReproducir(ActionListener listener);
    void escucharBotonVolver(ActionListener listener);
    void escucharBotonSalir(ActionListener listener);
}
