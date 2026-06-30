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
    private static final String RUTA_JUGADOR = "src/main/resources/bloques/jugador_frente.png";
    private static final String RUTA_CAJA_NORMAL = "src/main/resources/bloques/caja_normal.png";
    private static final String RUTA_CAJA_FRAGIL = "src/main/resources/bloques/caja_fragil.png";
    private static final String RUTA_CAJA_LLAVE = "src/main/resources/bloques/caja_llave.png";
    private static final String RUTA_PARED = "src/main/resources/bloques/pared.png";
    private static final String RUTA_REJA =  "src/main/resources/bloques/reja.png";
    private static final String RUTA_SUELO = "src/main/resources/bloques/suelo.png";
    private static final String RUTA_ACEITE = "src/main/resources/bloques/aceite2.png";
    private static final String RUTA_OBJETIVO = "src/main/resources/bloques/objetivo.png";
    private static final String RUTA_CERROJO = "src/main/resources/bloques/cerrojo.png";
    private static final String RUTA_MONEDA = "src/main/resources/bloques/moneda.png";
    private static final String RUTA_BOMBA =  "src/main/resources/bloques/bomba.png";

    private UIResources() {}

    public static BufferedImage cargarFondo() {
        try {
            return ImageIO.read(new File(RUTA_FONDO));
        } catch (IOException e) {
            System.err.println("Error al cargar imagen de fondo. Se reemplazará por un color plano.");
            return null;
        }
    }

    public static BufferedImage cargarBloque(char simbolo) {
        try {
            return switch (simbolo) {
                case 'P' -> ImageIO.read(new File(RUTA_PARED));
                case 'S' -> ImageIO.read(new File(RUTA_SUELO));
                case 'A' -> ImageIO.read(new File(RUTA_ACEITE));
                case 'D' -> ImageIO.read(new File(RUTA_OBJETIVO));
                case 'C' -> ImageIO.read(new File(RUTA_CERROJO));
                case 'R' -> ImageIO.read(new File(RUTA_REJA));
                case 'N' -> ImageIO.read(new File(RUTA_CAJA_NORMAL));
                case 'F' -> ImageIO.read(new File(RUTA_CAJA_FRAGIL));
                case 'K' -> ImageIO.read(new File(RUTA_CAJA_LLAVE));
                case 'J' -> ImageIO.read(new File(RUTA_JUGADOR));
                case 'B' -> ImageIO.read(new File(RUTA_BOMBA));
                case 'M' ->  ImageIO.read(new File(RUTA_MONEDA));
                default -> null;
            };
        } catch (IOException e) {
            System.err.println("Error al cargar imagen de bloque para el símbolo: " + simbolo);
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