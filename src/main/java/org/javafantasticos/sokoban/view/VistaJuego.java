package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.interfaces.IMoveCallback;
import org.javafantasticos.sokoban.interfaces.IVistaDeJuego;
import org.javafantasticos.sokoban.interfaces.IVistaHUD;
import org.javafantasticos.sokoban.model.Tablero;

import javax.swing.*;
import java.awt.*;

public class VistaJuego extends JPanel implements IVistaDeJuego {

    private final TableroPanel tableroPanel;
    private final HUDPanel hudPanel;
    private static final Color COLOR_FONDO = new Color(0x1e2a38);
    private final JPanel centerWrapper;

    public VistaJuego(Tablero tablero, Runnable onUndo, Runnable onReset,
                      Runnable onVolverAlMenu, IMoveCallback moveCallback) {
        super();
        this.setLayout(new BorderLayout());
        this.setBackground(COLOR_FONDO);

        this.tableroPanel = new TableroPanel(tablero);
        this.centerWrapper = new JPanel(new GridBagLayout());
        this.centerWrapper.setOpaque(false);
        this.centerWrapper.add(tableroPanel);
        this.add(centerWrapper, BorderLayout.CENTER);

        this.hudPanel = HUDPanel.getInstancia();
        this.hudPanel.setOpaque(false);
        this.add(hudPanel, BorderLayout.SOUTH);

        hudPanel.onUndo(e -> { onUndo.run(); requestFocusInWindow(); });
        hudPanel.onReset(e -> { onReset.run(); requestFocusInWindow(); });
        hudPanel.onVolverAlMenu(e -> onVolverAlMenu.run());

        moveCallback.setOnMove(tableroPanel::repaint);
    }

    @Override
    public TableroPanel getTableroPanel() {
        return tableroPanel;
    }

    @Override
    public void recuperarTablero() {
        if (tableroPanel.getParent() != this.centerWrapper) {
            centerWrapper.add(tableroPanel);
            revalidate();
            repaint();
        }
    }

    @Override
    public IVistaHUD getHudPanel() {
        return hudPanel;
    }
}
