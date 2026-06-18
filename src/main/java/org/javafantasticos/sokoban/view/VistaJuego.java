package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.controller.MovimientoTeclado;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del juego.
 * Ensambla el panel de dibujo y registra el listener de teclado.
 */
public class VistaJuego extends JPanel {

    private final TableroPanel tableroPanel;

    public VistaJuego(Tablero tablero) {
        super();
        this.setLayout(new CardLayout());
        this.tableroPanel = new TableroPanel(tablero);
        this.add(tableroPanel);
    }

    public void conectarTeclado(MovimientoTeclado teclado) {
        addKeyListener(teclado);
    }

    /**
     * Redibuja el tablero. Llamar después de cada movimiento.
     */
    /*@Override
    public void actualizar(Tablero tablero) {
        tableroPanel.actualizar(tablero);
    }*/

    public TableroPanel getTableroPanel() {
        return tableroPanel;
    }

}
