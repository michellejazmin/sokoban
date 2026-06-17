package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.interfaces.ElementoTablero;
import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.Tablero;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel que dibuja el estado actual del tablero.
 * Cada celda se representa con un color según su símbolo.
 */
public class TableroPanel extends JPanel implements Suscriptor {

    private static final int TAMANIO_CELDA = 50;

    private Tablero tablero;

    public TableroPanel(Tablero tablero) {
        this.tablero = tablero;
        int ancho = tablero.getGrilla().getFirst().size() * TAMANIO_CELDA;
        int alto  = tablero.getGrilla().size() * TAMANIO_CELDA;
        setPreferredSize(new Dimension(ancho, alto));
    }

    /**
     * Redibuja el tablero. Llamar después de cada movimiento.
     */
    @Override
    public void actualizar(Tablero tablero) {
        this.tablero = tablero;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<List<ElementoTablero>> grilla = tablero.getGrilla();

        for (int fila = 0; fila < grilla.size(); fila++) {
            for (int col = 0; col < grilla.get(fila).size(); col++) {
                char simbolo = grilla.get(fila).get(col).getSimbolo();
                int x = col  * TAMANIO_CELDA;
                int y = fila * TAMANIO_CELDA;

                dibujarCelda(g, simbolo, x, y);
            }
        }
    }

    private void dibujarCelda(Graphics g, char simbolo, int x, int y) {
        Color fondo;
        String texto;

        switch (simbolo) {
            case 'P' -> { fondo = new Color(60, 60, 60);    texto = "";  }  // Pared
            case 'S' -> { fondo = new Color(210, 180, 140); texto = "";  }  // Suelo
            case 'A' -> { fondo = new Color(100, 180, 240); texto = "~"; }  // Aceite
            case 'D' -> { fondo = new Color(144, 238, 144); texto = "★"; }  // Destino
            case 'C' -> { fondo = new Color(210, 180, 140); texto = "🔒";}  // Cerrojo
            case 'R' -> { fondo = new Color(180, 100, 50);  texto = "⛩"; }  // Reja
            case 'N' -> { fondo = new Color(210, 140, 60);  texto = "■"; }  // Caja normal
            case 'F' -> { fondo = new Color(240, 200, 100); texto = "◧"; }  // Caja frágil
            case 'K' -> { fondo = new Color(255, 220, 50);  texto = "🔑";}  // Caja llave
            case 'J' -> { fondo = new Color(70, 130, 180);  texto = "☺"; }  // Jugador
            default  -> { fondo = Color.LIGHT_GRAY;         texto = "?"; }
        }

        // Fondo de la celda
        g.setColor(fondo);
        g.fillRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        // Borde
        g.setColor(new Color(0, 0, 0, 40));
        g.drawRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        // Texto / símbolo
        if (!texto.isEmpty()) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Dialog", Font.BOLD, 20));
            FontMetrics fm = g.getFontMetrics();
            int tx = x + (TAMANIO_CELDA - fm.stringWidth(texto)) / 2;
            int ty = y + (TAMANIO_CELDA - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(texto, tx, ty);
        }
    }
}
