package org.javafantasticos.sokoban.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOverPanel extends JPanel {
    private final JLabel tituloLabel;
    private final JLabel motivoLabel;
    private final JButton botonVolver;
    private BufferedImage backgroundImage;

    public GameOverPanel(String motivo) {
        super();
        this.setLayout(new GridBagLayout());

        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/pixel-8bit-brick-wall-blue.jpg"));
        } catch (IOException e) {
            System.err.println("Error al cargar imagen de fondo. Se reemplazará por un color plano.");
            this.setBackground(new Color(110, 197, 226));
        }

        Font titleFont;
        Font bodyFont;
        try {
            File fontFile = new File("src/main/resources/font/JetBrainsMono-Bold.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            titleFont = base.deriveFont(Font.BOLD, 48);
            bodyFont = base.deriveFont(Font.PLAIN, 20);
        } catch (IOException | FontFormatException e) {
            titleFont = new Font("Arial", Font.BOLD, 48);
            bodyFont = new Font("Arial", Font.PLAIN, 20);
        }

        tituloLabel = new JLabel("Game Over");
        tituloLabel.setFont(titleFont);
        tituloLabel.setForeground(new Color(0xC0, 0x39, 0x2B));

        motivoLabel = new JLabel(motivo);
        motivoLabel.setFont(bodyFont);
        motivoLabel.setForeground(Color.WHITE);

        botonVolver = new JButton("Volver al men\u00fa principal");
        botonVolver.setFont(bodyFont);
        botonVolver.setBackground(new Color(0x5D, 0x7B, 0x93));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.setFocusPainted(false);
        botonVolver.setBorderPainted(false);
        botonVolver.setOpaque(true);
        botonVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        add(tituloLabel, gbc);
        gbc.gridy = 1;
        add(motivoLabel, gbc);
        gbc.gridy = 2;
        add(botonVolver, gbc);
    }

    public void setMotivo(String motivo) {
        motivoLabel.setText(motivo);
    }

    public void escucharBotonVolver(ActionListener listener) {
        botonVolver.addActionListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.backgroundImage != null) {
            int targetY = 0;

            while (this.getHeight() > targetY) {
                int targetX = 0;
                while (this.getWidth() > targetX) {
                    g.drawImage(this.backgroundImage, targetX, targetY, this.backgroundImage.getWidth(), this.backgroundImage.getHeight(), null);
                    targetX += this.backgroundImage.getWidth();
                }
                targetY += this.backgroundImage.getHeight();
            }
        }
    }
}
