package org.example.view;

import org.example.controller.GameController;
import org.example.model.Tablero;
import org.example.model.player.MovimientoTeclado;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Ventana principal del juego.
 * Ensambla el panel de dibujo y registra el listener de teclado.
 */
public class VistaJuego extends JFrame {

    private final TableroPanel tableroPanel;

    public VistaJuego(Tablero tablero, GameController controller) {
        super(tablero.getNombre());

        this.tableroPanel = new TableroPanel(tablero);

        // Registrar el listener de teclado
        MovimientoTeclado movimientoTeclado = new MovimientoTeclado(controller);
        addKeyListener(movimientoTeclado);

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(tableroPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Repaint automático hasta implementar Observer
        new Timer(16, e -> tableroPanel.repaint()).start();
    }

    /**
     * Redibuja el tablero. Llamar después de cada movimiento.
     */
    public void actualizar(Tablero tablero) {
        tableroPanel.actualizar(tablero);
    }
}
