package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.interfaces.ControladorVista;
import org.javafantasticos.sokoban.interfaces.VistaDeJuego;
import org.javafantasticos.sokoban.interfaces.VistaHUD;
import org.javafantasticos.sokoban.model.Tablero;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del juego.
 * Ensambla el panel de dibujo, el HUD y registra el listener de teclado.
 */
    public class VistaJuego extends JPanel implements VistaDeJuego {
    //private static VistaJuego instancia;

    private final TableroPanel tableroPanel;
    private final HUDPanel hudPanel;
    private static final Color COLOR_FONDO = new Color(0x1e2a38);
    private final JPanel centerWrapper;

    public VistaJuego(Tablero tablero, ControladorVista controller) {
        super();
        this.setLayout(new BorderLayout());
        this.setBackground(COLOR_FONDO);

        this.tableroPanel = new TableroPanel(tablero, controller::getOrientacion);
        this.centerWrapper = new JPanel(new GridBagLayout());
        this.centerWrapper.setOpaque(false);
        this.centerWrapper.add(tableroPanel);
        this.add(centerWrapper, BorderLayout.CENTER);

        this.hudPanel = HUDPanel.getInstancia();
        this.hudPanel.setOpaque(false);
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

    @Override
    public TableroPanel getTableroPanel() {
        return tableroPanel;
    }

    /**
     * Reinserta el TableroPanel en la vista de juego. Necesario porque la pantalla
     * de reproducción lo toma prestado (un componente Swing sólo puede tener un
     * contenedor a la vez).
     */
    @Override
    public void recuperarTablero() {
        if (tableroPanel.getParent() != this.centerWrapper) {
            centerWrapper.add(tableroPanel);
            revalidate();
            repaint();
        }
    }

    @Override
    public VistaHUD getHudPanel() {
        return hudPanel;
    }
}
