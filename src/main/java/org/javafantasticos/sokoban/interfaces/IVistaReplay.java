package org.javafantasticos.sokoban.interfaces;

import java.awt.event.ActionListener;

public interface IVistaReplay {
    void cargar(ISuscriptor board, IReproductorVista rep, boolean mostrarContinuar,
                ActionListener alVolver, ActionListener alContinuar);
}
