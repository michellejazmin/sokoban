package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.controller.GameController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class HUDPanel extends JPanel {

    private static Font HUD_FONT;

    private final JLabel nivelLabel;
    private final JLabel pasosLabel;
    private final JLabel movCajasLabel;
    private final JLabel cajasObjetivoLabel;
    private final JLabel limiteUndoLabel;
    private final JLabel undosRestantesLabel;
    private final JButton undoButton;
    private final JButton resetButton;

    public HUDPanel() {
        cargarFuente();

        setBackground(new Color(0x1E, 0x2A, 0x38));
        setLayout(new GridLayout(4, 1, 0, 2));
        setBorder(new EmptyBorder(6, 12, 6, 12));

        nivelLabel = crearLabel("Nivel: -");
        pasosLabel = crearLabel("Pasos: 0");
        movCajasLabel = crearLabel("Mov. cajas: 0");
        cajasObjetivoLabel = crearLabel("Cajas en obj: 0/0");
        limiteUndoLabel = crearLabel("Limite undo: -");
        undosRestantesLabel = crearLabel("Undos restantes: -/-");

        undoButton = crearBoton("\u2190 Undo", new Color(0x5D, 0x7B, 0x93));
        resetButton = crearBoton("\u21BB Reiniciar", new Color(0xC0, 0x39, 0x2B));

        add(crearFila(nivelLabel, pasosLabel));
        add(crearFila(movCajasLabel, cajasObjetivoLabel));
        add(crearFila(limiteUndoLabel, undosRestantesLabel));
        add(crearFila(undoButton, resetButton));
    }

    private JPanel crearFila(Component... componentes) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        fila.setOpaque(false);
        for (Component c : componentes) {
            fila.add(c);
        }
        return fila;
    }

    private void cargarFuente() {
        if (HUD_FONT != null) return;
        try {
            File fontFile = new File("src/main/resources/font/JetBrainsMono-Bold.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            HUD_FONT = base.deriveFont(Font.PLAIN, 14f);
        } catch (IOException | FontFormatException e) {
            HUD_FONT = new Font("Monospaced", Font.PLAIN, 14);
        }
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(HUD_FONT);
        label.setForeground(Color.WHITE);
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

    public void actualizar(GameController ctrl) {
        nivelLabel.setText("Nivel: " + ctrl.getNivelActual() + "/" + ctrl.getTotalNiveles());
        pasosLabel.setText("Pasos: " + ctrl.getSteps());
        movCajasLabel.setText("Mov. cajas: " + ctrl.getPushes());
        cajasObjetivoLabel.setText("Cajas en obj: " + ctrl.getCajasEnDestino() + "/" + ctrl.getTotalCajas());
        limiteUndoLabel.setText("Limite undo: " + ctrl.getUndoStepSize() + " pasos");
        undosRestantesLabel.setText("Undos restantes: " + ctrl.getUndoRemaining() + "/" + ctrl.getMaxUndoUses());
        undoButton.setEnabled(ctrl.canUndo());
    }

    public void onUndo(ActionListener listener) {
        undoButton.addActionListener(listener);
    }

    public void onReset(ActionListener listener) {
        resetButton.addActionListener(listener);
    }
}