package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.controller.GameController;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.player.Orientacion;
import java.util.function.Supplier;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del juego.
 * Ensambla el panel de dibujo, el HUD y registra el listener de teclado.
 *
 * Patrón Singleton: única vista de juego durante toda la partida.
 */
public class VistaJuego extends JPanel {
    //private static VistaJuego instancia;

    private final TableroPanel tableroPanel;
    private final HUDPanel hudPanel;

    public VistaJuego(Tablero tablero, GameController controller) {
        super();
        this.setLayout(new BorderLayout());

        this.tableroPanel = new TableroPanel(tablero, controller::getOrientacion);
        this.add(tableroPanel, BorderLayout.CENTER);

        this.hudPanel = HUDPanel.getInstancia();
        this.add(hudPanel, BorderLayout.SOUTH);

        hudPanel.onUndo(e -> {
            controller.undo();
            requestFocusInWindow();
        });

        hudPanel.onReset(e -> {
            controller.reiniciarNivel();
            requestFocusInWindow();
        });

        hudPanel.onVolverAlMenu(e -> {
            controller.volverAlMenu();
        });

        controller.setOnMove(tableroPanel::repaint);
    }

    /*public static VistaJuego getInstancia(Tablero tablero, GameController controller) {
        if (instancia == null) {
            instancia = new VistaJuego(tablero, controller);
        }
        return instancia;
    }*/

    public TableroPanel getTableroPanel() {
        return tableroPanel;
    }

    /**
     * Reinserta el TableroPanel en la vista de juego. Necesario porque la pantalla
     * de reproducción lo toma prestado (un componente Swing sólo puede tener un
     * contenedor a la vez).
     */
    public void recuperarTablero() {
        if (tableroPanel.getParent() != this) {
            add(tableroPanel, BorderLayout.CENTER);
            revalidate();
            repaint();
        }
    }

    public HUDPanel getHudPanel() {
        return hudPanel;
    }
}
