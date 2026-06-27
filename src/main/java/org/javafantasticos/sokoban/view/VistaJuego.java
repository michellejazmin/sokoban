package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.controller.GameController;
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
    private final JButton undoButton;

    public VistaJuego(Tablero tablero, GameController controller) {
        super();
        this.setLayout(new BorderLayout());

        this.tableroPanel = new TableroPanel(tablero);
        this.add(tableroPanel, BorderLayout.CENTER);

        undoButton = new JButton("Undo");
        undoButton.setFocusable(false);
        undoButton.setEnabled(controller.canUndo());
        undoButton.addActionListener(e -> {
            controller.undo();
            requestFocusInWindow();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottomPanel.add(undoButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        controller.setOnMove(() -> {
            tableroPanel.repaint();
            undoButton.setEnabled(controller.canUndo());
        });
    }

    public void conectarTeclado(MovimientoTeclado teclado) {
        addKeyListener(teclado);
    }

    public TableroPanel getTableroPanel() {
        return tableroPanel;
    }

}
