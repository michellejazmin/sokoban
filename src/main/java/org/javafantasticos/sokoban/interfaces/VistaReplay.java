package org.javafantasticos.sokoban.interfaces;

import org.javafantasticos.sokoban.controller.ReproductorPartida;

import java.awt.event.ActionListener;

public interface VistaReplay {
    void cargar(Suscriptor board, ReproductorPartida rep, boolean mostrarContinuar,
                ActionListener alVolver, ActionListener alContinuar);
}
