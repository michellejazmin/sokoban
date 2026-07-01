package org.javafantasticos.sokoban.interfaces;

import java.awt.event.ActionListener;

public interface PantallaGameOver {
    void setMotivo(String motivo);
    void escucharBotonReproducir(ActionListener listener);
    void escucharBotonVolver(ActionListener listener);
    void escucharBotonSalir(ActionListener listener);
}
