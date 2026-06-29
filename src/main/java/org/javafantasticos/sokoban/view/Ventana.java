package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {
    private final JPanel contenedor;
    private final CardLayout cardLayout;

    public Ventana() {
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

}
