package org.javafantasticos.sokoban.view;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel {
    private final JButton botonJugar;
    private final JButton botonSalir;
    private BufferedImage backgroundImage;

    public Menu() {
        super();
        this.setLayout(new GridBagLayout());

        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/pixel-8bit-brick-wall-blue.jpg"));
        } catch (IOException e) {
            System.err.println("Error al cargar imagen de fondo. Se reemplazará por un color plano.");
            this.setBackground(new Color(110, 197, 226));
        }

        Font defaultFont;
        try {
            File fontFile = new File("src/main/resources/font/JetBrainsMono-Regular.ttf");
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            defaultFont = baseFont.deriveFont(Font.PLAIN, 20);
        } catch (IOException | FontFormatException e) {
            System.err.println("Error al cargar fuente. Se reemplazará por fuente predeterminada.");
            System.err.println(e.getMessage());
            defaultFont = new Font("Arial", Font.PLAIN, 20);
        }

        JLabel titulo = new JLabel("Sokoban");
        titulo.setFont(defaultFont);
        titulo.setForeground(Color.WHITE);

        this.botonJugar = new JButton("Iniciar juego");
        this.botonJugar.setFont(defaultFont);

        this.botonSalir = new JButton("Salir");
        this.botonSalir.setFont(defaultFont);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0; add(titulo, gbc);
        gbc.gridy = 1; add(botonJugar, gbc);
        gbc.gridy = 2; add(botonSalir, gbc);
    }

    // Métodos para que el controlador escuche los botones
    public void escucharBotonJugar(ActionListener listener) {
        botonJugar.addActionListener(listener);
    }

    public void escucharBotonSalir(ActionListener listener) {
        botonSalir.addActionListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.backgroundImage != null) {
            int targetY = 0;

            while (this.getHeight() > targetY) {

                int targetX = 0;
                while (this.getWidth() > targetX) {
                    g.drawImage(this.backgroundImage, targetX, targetY, this.backgroundImage.getWidth(), this.backgroundImage.getHeight() , null);

                    targetX += this.backgroundImage.getWidth();
                }

                targetY += this.backgroundImage.getHeight();
            }
        }
    }

}
