package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseOverlayPanel extends JPanel {
    protected final JLabel tituloLabel;
    protected final JLabel mensajeLabel;
    protected final BufferedImage backgroundImage;

    protected BaseOverlayPanel(String titulo, String mensaje) {
        super(new GridBagLayout());

        backgroundImage = UIResources.cargarFondo();
        if (backgroundImage == null) setBackground(new Color(110, 197, 226));

        Font titleFont = UIResources.cargarFuenteBold(48);
        Font bodyFont = UIResources.cargarFuenteRegular(20);

        tituloLabel = new JLabel(titulo);
        tituloLabel.setFont(titleFont);
        tituloLabel.setForeground(Color.WHITE);

        mensajeLabel = new JLabel(mensaje);
        mensajeLabel.setFont(bodyFont);
        mensajeLabel.setForeground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridy = 0; add(tituloLabel, gbc);
        gbc.gridy = 1; add(mensajeLabel, gbc);
    }

    protected final void agregarBotonEn(int fila, JButton boton) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(boton, gbc);
    }

    protected final JButton crearBoton(String texto, Font font, Color fondo) {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIResources.dibujarFondoMosaico(g, backgroundImage, getWidth(), getHeight());
    }
}
