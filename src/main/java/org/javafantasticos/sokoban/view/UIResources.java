package org.javafantasticos.sokoban.view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UIResources {

    private static final String RUTA_FONDO = "src/main/resources/pixel-8bit-brick-wall-blue.jpg";
    private static final String RUTA_FUENTE_REGULAR = "src/main/resources/font/JetBrainsMono-Regular.ttf";
    private static final String RUTA_FUENTE_BOLD = "src/main/resources/font/JetBrainsMono-Bold.ttf";

    private UIResources() {}

    public static BufferedImage cargarFondo() {
        try {
            return ImageIO.read(new File(RUTA_FONDO));
        } catch (IOException e) {
            System.err.println("Error al cargar imagen de fondo. Se reemplazará por un color plano.");
            return null;
        }
    }

    public static Font cargarFuenteRegular(float tamanio) {
        try {
            Font base = Font.createFont(Font.TRUETYPE_FONT, new File(RUTA_FUENTE_REGULAR));
            return base.deriveFont(Font.PLAIN, tamanio);
        } catch (IOException | FontFormatException e) {
            System.err.println("Error al cargar fuente. Se reemplazará por fuente predeterminada.");
            return new Font("Arial", Font.PLAIN, (int) tamanio);
        }
    }

    public static Font cargarFuenteBold(float tamanio) {
        try {
            Font base = Font.createFont(Font.TRUETYPE_FONT, new File(RUTA_FUENTE_BOLD));
            return base.deriveFont(Font.BOLD, tamanio);
        } catch (IOException | FontFormatException e) {
            return new Font("Arial", Font.BOLD, (int) tamanio);
        }
    }

    public static void dibujarFondoMosaico(Graphics g, BufferedImage imagen, int ancho, int alto) {
        if (imagen == null) return;
        int targetY = 0;
        while (alto > targetY) {
            int targetX = 0;
            while (ancho > targetX) {
                g.drawImage(imagen, targetX, targetY, imagen.getWidth(), imagen.getHeight(), null);
                targetX += imagen.getWidth();
            }
            targetY += imagen.getHeight();
        }
    }
}