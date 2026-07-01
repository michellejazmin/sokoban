package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;

/**
 * Patrón Singleton: la ventana principal del juego es única durante toda la
 * ejecución; tener más de una rompería el CardLayout y el ciclo de vida Swing.
 */
public class Ventana extends JFrame {
    private static Ventana instancia;

    private final JPanel contenedor;
    private final CardLayout cardLayout;

    private Ventana() {
        setTitle("Sokoban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(400, 400));
        setResizable(false);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);

        add(contenedor);
        setVisible(true);
    }

    public static Ventana getInstancia() {
        if (instancia == null) {
            instancia = new Ventana();
        }
        return instancia;
    }

    public void agregarPantalla(JPanel pantalla, String nombre) {
        contenedor.add(pantalla, nombre);
    }

    private void mostrarPantalla(String nombre) {
        cardLayout.show(contenedor, nombre);
        pack();
    }

    public void mostrarJuego() {
        mostrarPantalla("JUEGO");
    }

    public void mostrarMenu() {
        mostrarPantalla("MENU");
    }

    public void mostrarGameOver() {
        mostrarPantalla("GAMEOVER");
    }

    public void mostrarVictoria() {
        mostrarPantalla("VICTORIA");
    }

    public void mostrarPasoNivel() {
        mostrarPantalla("PASONIVEL");
    }

    public void mostrarReplay() {
        mostrarPantalla("REPLAY");
    }

}
