package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.controller.GameController;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.controller.MovimientoTeclado;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del juego.
 * Ensambla el panel de dibujo, el HUD y registra el listener de teclado.
 */
public class VistaJuego extends JPanel {

    private final TableroPanel tableroPanel;
    private final HUDPanel hudPanel;

    public VistaJuego(Tablero tablero, GameController controller) {
        super();
        this.setLayout(new BorderLayout());

        this.tableroPanel = new TableroPanel(tablero);
        this.add(tableroPanel, BorderLayout.CENTER);

        this.hudPanel = new HUDPanel();
        this.add(hudPanel, BorderLayout.SOUTH);

        hudPanel.onUndo(e -> {
            controller.undo();
            requestFocusInWindow();
        });

        hudPanel.onReset(e -> {
            controller.reiniciarNivel();
            requestFocusInWindow();
        });

        controller.setOnMove(() -> tableroPanel.repaint());
    }

    public void conectarTeclado(MovimientoTeclado teclado) {
        addKeyListener(teclado);
    }

    public TableroPanel getTableroPanel() {
        return tableroPanel;
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }
}
