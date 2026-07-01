package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.interfaces.ControladorVista;
import org.javafantasticos.sokoban.interfaces.VistaHUD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class HUDPanel extends JPanel implements VistaHUD {
    private static HUDPanel instancia;

    private final Font HUD_FONT;
    private final Font SCORE_FONT;

    private final JLabel scoreLabel;
    private final JLabel nivelLabel;
    private final JLabel pasosLabel;
    private final JLabel movCajasLabel;
    private final JLabel cajasObjetivoLabel;
    private final JLabel limiteUndoLabel;
    private final JLabel undosRestantesLabel;
    private final JButton undoButton;
    private final JButton resetButton;
    private final JButton volverAlMenuButton;

    private HUDPanel() {
        HUD_FONT   = UIResources.cargarFuenteRegular(14);
        SCORE_FONT = UIResources.cargarFuenteBold(18);

        setLayout(new GridLayout(5, 1, 0, 2));
        setBorder(new EmptyBorder(6, 12, 6, 12));

        scoreLabel          = crearLabel("Puntaje: 0", SCORE_FONT, new Color(0xFF, 0xD7, 0x00));
        nivelLabel          = crearLabel("Nivel: -");
        pasosLabel          = crearLabel("Pasos: 0");
        movCajasLabel       = crearLabel("Mov. cajas: 0");
        cajasObjetivoLabel  = crearLabel("Cajas en obj: 0/0");
        limiteUndoLabel     = crearLabel("Limite undo: -");
        undosRestantesLabel = crearLabel("Undos restantes: -/-");

        undoButton        = crearBoton("Deshacer",        new Color(0x5D, 0x7B, 0x93));
        resetButton       = crearBoton("Reiniciar",       new Color(0xD4, 0xA0, 0x17));
        volverAlMenuButton = crearBoton("Volver al menú", new Color(0xC0, 0x39, 0x2B));

        add(crearFilaCentrada(scoreLabel));
        add(crearFila(nivelLabel, pasosLabel));
        add(crearFila(movCajasLabel, cajasObjetivoLabel));
        add(crearFila(limiteUndoLabel, undosRestantesLabel));
        add(crearFila(undoButton, resetButton, volverAlMenuButton));
    }

    public static HUDPanel getInstancia() {
        if (instancia == null) instancia = new HUDPanel();
        return instancia;
    }

    private JPanel crearFila(Component... componentes) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        fila.setOpaque(false);
        for (Component c : componentes) fila.add(c);
        return fila;
    }

    private JPanel crearFilaCentrada(Component unico) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        fila.setOpaque(false);
        fila.add(unico);
        return fila;
    }

    private JLabel crearLabel(String texto) {
        return crearLabel(texto, HUD_FONT, Color.WHITE);
    }

    private JLabel crearLabel(String texto, Font font, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(HUD_FONT);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }

    public void actualizar(ControladorVista ctrl) {
        scoreLabel.setText("Puntaje: " + ctrl.getScore());
        nivelLabel.setText("Nivel: " + ctrl.getNivelActual() + "/" + ctrl.getTotalNiveles());
        pasosLabel.setText("Pasos: " + ctrl.getSteps());
        movCajasLabel.setText("Mov. cajas: " + ctrl.getPushes());
        cajasObjetivoLabel.setText("Cajas en obj: " + ctrl.getCajasEnDestino() + "/" + ctrl.getTotalCajas());
        limiteUndoLabel.setText("Límite deshacer: " + ctrl.getUndoStepSize() + " pasos");
        undosRestantesLabel.setText("Deshacer restantes: " + ctrl.getUndoRemaining() + "/" + ctrl.getMaxUndoUses());
        undoButton.setEnabled(ctrl.canUndo());
    }

    public void onUndo(ActionListener listener)         { undoButton.addActionListener(listener); }
    public void onReset(ActionListener listener)        { resetButton.addActionListener(listener); }
    public void onVolverAlMenu(ActionListener listener) { volverAlMenuButton.addActionListener(listener); }
}