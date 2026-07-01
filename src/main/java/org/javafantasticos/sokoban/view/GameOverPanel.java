package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class GameOverPanel extends JPanel {
    private static GameOverPanel instancia;

    private final JLabel tituloLabel;
    private final JLabel motivoLabel;
    private final JButton botonReproducir;
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
        tituloLabel.setForeground(Color.WHITE);

        motivoLabel = new JLabel(motivo);
        motivoLabel.setFont(bodyFont);
        motivoLabel.setForeground(Color.WHITE);

        botonReproducir = crearBoton("Reproducir partida", bodyFont, new Color(0x27, 0xAE, 0x60));
        botonVolver = crearBoton("Volver al menú principal", bodyFont, new Color(0x5D, 0x7B, 0x93));
        botonSalir = crearBoton("Salir", bodyFont, new Color(0xC0, 0x39, 0x2B));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0; add(tituloLabel, gbc);
        gbc.gridy = 1; add(motivoLabel, gbc);
        gbc.gridy = 2; add(botonReproducir, gbc);
        gbc.gridy = 3; add(botonVolver, gbc);
        gbc.gridy = 4; add(botonSalir, gbc);
    }

    public static GameOverPanel getInstancia(String motivo) {
        if (instancia == null) instancia = new GameOverPanel(motivo);
        return instancia;
    }

    private JButton crearBoton(String texto, Font font, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(font);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void setMotivo(String motivo) { motivoLabel.setText(motivo); }
    public void escucharBotonReproducir(ActionListener listener) { botonReproducir.addActionListener(listener); }
    public void escucharBotonVolver(ActionListener listener) { botonVolver.addActionListener(listener); }
    public void escucharBotonSalir(ActionListener listener) { botonSalir.addActionListener(listener); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIResources.dibujarFondoMosaico(g, backgroundImage, getWidth(), getHeight());
    }
}
