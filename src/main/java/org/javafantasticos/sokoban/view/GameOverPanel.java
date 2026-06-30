package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class GameOverPanel extends JPanel {
    private static GameOverPanel instancia;

    private final JLabel tituloLabel;
    private final JLabel motivoLabel;
    private final JButton botonVolver;
    private final JButton botonSalir;
    private final BufferedImage backgroundImage;

    private GameOverPanel(String motivo) {
        super();
        this.setLayout(new GridBagLayout());

        backgroundImage = UIResources.cargarFondo();
        if (backgroundImage == null) this.setBackground(new Color(110, 197, 226));

        Font titleFont = UIResources.cargarFuenteBold(48);
        Font bodyFont  = UIResources.cargarFuenteRegular(20);

        tituloLabel = new JLabel("Game Over");
        tituloLabel.setFont(titleFont);
        tituloLabel.setForeground(new Color(0xC0, 0x39, 0x2B));

        motivoLabel = new JLabel(motivo);
        motivoLabel.setFont(bodyFont);
        motivoLabel.setForeground(Color.WHITE);

        botonVolver = new JButton("Volver al menú principal");
        botonVolver.setFont(bodyFont);
        botonVolver.setBackground(new Color(0x5D, 0x7B, 0x93));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.setBorderPainted(false);
        botonVolver.setOpaque(true);
        botonVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        botonSalir = new JButton("Salir");
        botonSalir.setFont(bodyFont);
        botonSalir.setBackground(new Color(0xC0, 0x39, 0x2B));
        botonSalir.setForeground(Color.WHITE);
        botonSalir.setFocusPainted(false);
        botonSalir.setBorderPainted(false);
        botonSalir.setOpaque(true);
        botonSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0; add(tituloLabel, gbc);
        gbc.gridy = 1; add(motivoLabel, gbc);
        gbc.gridy = 2; add(botonVolver, gbc);
        gbc.gridy = 3; add(botonSalir, gbc);
    }

    public static GameOverPanel getInstancia(String motivo) {
        if (instancia == null) instancia = new GameOverPanel(motivo);
        return instancia;
    }

    public void setMotivo(String motivo) { motivoLabel.setText(motivo); }
    public void escucharBotonVolver(ActionListener listener) { botonVolver.addActionListener(listener); }
    public void escucharBotonSalir(ActionListener listener) { botonSalir.addActionListener(listener); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIResources.dibujarFondoMosaico(g, backgroundImage, getWidth(), getHeight());
    }
}