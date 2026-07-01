package org.javafantasticos.sokoban.interfaces;

import java.awt.event.ActionListener;

public interface IPantallaPasoNivel {
    void setMensaje(String mensaje);
    void escucharBotonSiguiente(ActionListener listener);
    void escucharBotonReproducir(ActionListener listener);
    void escucharBotonVolver(ActionListener listener);
}
