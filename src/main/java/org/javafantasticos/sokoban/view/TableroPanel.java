package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.Tablero;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Panel que dibuja el estado actual del tablero.
 * Cada celda se representa con un color según su símbolo.
 *
 * Patrón Singleton: único panel de dibujo del tablero durante toda la partida.
 * Al cambiar de nivel se actualiza vía {@link #actualizar(Tablero)} (Observer).
 */
public class TableroPanel extends JPanel implements Suscriptor {
    private static TableroPanel instancia;

    private static final int TAMANIO_CELDA = 50;

    private Tablero tablero;

    private TableroPanel(Tablero tablero) {
        this.tablero = tablero;
        int ancho = tablero.getGrillaInferior().getFirst().size() * TAMANIO_CELDA;
        int alto  = tablero.getGrillaInferior().size() * TAMANIO_CELDA;
        setPreferredSize(new Dimension(ancho, alto));
    }

    public static TableroPanel getInstancia(Tablero tablero) {
        if (instancia == null) {
            instancia = new TableroPanel(tablero);
        }
        return instancia;
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
        List<List<ElementoBase>> grillaInferior = tablero.getGrillaInferior();
        List<List<ElementoBase>> grillaSuperior = tablero.getGrillaSuperior();

        for (int fila = 0; fila < grillaInferior.size(); fila++) {
            for (int col = 0; col < grillaInferior.get(fila).size(); col++) {
                char simbolo = (grillaSuperior.get(fila).get(col) == null) ?
                        grillaInferior.get(fila).get(col).getSimbolo() :
                        grillaSuperior.get(fila).get(col).getSimbolo();

                int x = col  * TAMANIO_CELDA;
                int y = fila * TAMANIO_CELDA;

                BufferedImage imagen = UIResources.cargarBloque(simbolo);
                dibujarCelda(g, simbolo, x, y, imagen);
            }
        }
    }

    private void dibujarCelda(Graphics g, char simbolo, int x, int y, BufferedImage imagen) {
        Color fondo;
        String texto;

        switch (simbolo) {
            case 'P' -> { fondo = new Color(0xFF5342);    texto = "";  }  // Pared
            case 'S' -> { fondo = new Color(210, 180, 140); texto = "";  }  // Suelo
            case 'A' -> { fondo = new Color(0xaa733c); texto = "~"; }  // Aceite
            case 'D' -> { fondo = new Color(0x758c8e); texto = "★"; }  // Destino
            case 'C' -> { fondo = new Color(0x758c8e); texto = "🔒";}  // Cerrojo
            case 'R' -> { fondo = new Color(0x758c8e);  texto = "⛩"; }  // Reja cerrada
            case 'N' -> { fondo = new Color(0x758c8e);  texto = "■"; }  // Caja normal
            case 'F' -> { fondo = new Color(0x758c8e); texto = "◧"; }  // Caja frágil
            case 'K' -> { fondo = new Color(0x758c8e);  texto = "🔑";}  // Caja llave
            case 'M' -> { fondo = new Color(255, 215, 0);   texto = "$"; }  // Moneda
            case 'B' -> { fondo = new Color(80, 20, 20);    texto = "💣";}  // Bomba
            case 'J' -> { fondo = new Color(0x0000, true);  texto = "☺"; }  // Jugador
            default  -> { fondo = Color.LIGHT_GRAY;         texto = "?"; }
        }

        // Fondo de la celda
        g.setColor(fondo);
        g.fillRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        // Borde
        //g.setColor(new Color(0, 0, 0, 40));
        //g.drawRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        if (imagen != null) {
            g.drawImage(imagen, x, y, TAMANIO_CELDA, TAMANIO_CELDA, null);
        } else if (!texto.isEmpty()) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Dialog", Font.BOLD, 20));
                FontMetrics fm = g.getFontMetrics();
                int tx = x + (TAMANIO_CELDA - fm.stringWidth(texto)) / 2;
                int ty = y + (TAMANIO_CELDA - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(texto, tx, ty);
        }
    }
}
