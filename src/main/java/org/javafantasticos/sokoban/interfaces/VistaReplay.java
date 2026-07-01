package org.javafantasticos.sokoban.interfaces;

import java.awt.event.ActionListener;

public interface VistaReplay {
    void cargar(Suscriptor board, IReproductorVista rep, boolean mostrarContinuar,
                ActionListener alVolver, ActionListener alContinuar);
}
