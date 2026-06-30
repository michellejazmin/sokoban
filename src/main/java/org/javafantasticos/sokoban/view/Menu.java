package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Menu extends JPanel {
    private static Menu instancia;

    private final JButton botonJugar;
    private final JButton botonSalir;
    private final BufferedImage backgroundImage;

    private Menu() {
        super();
        this.setLayout(new GridBagLayout());

        backgroundImage = UIResources.cargarFondo();
        if (backgroundImage == null) this.setBackground(new Color(110, 197, 226));

        Font defaultFont = UIResources.cargarFuenteRegular(20);
        Font tituloFont  = UIResources.cargarFuenteBold(48);

        JLabel titulo = new JLabel("Sokoban");
        titulo.setFont(tituloFont);
        titulo.setForeground(Color.WHITE);

        this.botonJugar = new JButton("Iniciar juego");
        this.botonJugar.setFont(defaultFont);
        this.botonJugar.setBackground(new Color(0x5D, 0x7B, 0x93));
        this.botonJugar.setForeground(Color.WHITE);
        this.botonJugar.setFocusPainted(false);
        this.botonJugar.setBorderPainted(false);
        this.botonJugar.setOpaque(true);
        this.botonJugar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.botonSalir = new JButton("Salir");
        this.botonSalir.setFont(defaultFont);
        this.botonSalir.setBackground(new Color(0xC0, 0x39, 0x2B));
        this.botonSalir.setForeground(Color.WHITE);
        this.botonSalir.setFocusPainted(false);
        this.botonSalir.setBorderPainted(false);
        this.botonSalir.setOpaque(true);
        this.botonSalir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0; add(titulo, gbc);
        gbc.gridy = 1; add(botonJugar, gbc);
        gbc.gridy = 2; add(botonSalir, gbc);
    }

    public static Menu getInstancia() {
        if (instancia == null) instancia = new Menu();
        return instancia;
    }

    public void escucharBotonJugar(ActionListener listener) { botonJugar.addActionListener(listener); }
    public void escucharBotonSalir(ActionListener listener) { botonSalir.addActionListener(listener); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIResources.dibujarFondoMosaico(g, backgroundImage, getWidth(), getHeight());
    }
}