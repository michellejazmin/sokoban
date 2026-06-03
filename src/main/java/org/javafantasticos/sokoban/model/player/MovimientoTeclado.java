package org.javafantasticos.sokoban.model.player;

import org.javafantasticos.sokoban.controller.GameController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovimientoTeclado implements KeyListener {
    private final GameController controller;

    public MovimientoTeclado(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita para este caso
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Character.toLowerCase(e.getKeyChar())) {
            case 'w' -> controller.moverArriba();
            case 'a' -> controller.moverIzquierda();
            case 's' -> controller.moverAbajo();
            case 'd' -> controller.moverDerecha();
            default  -> { /* Ignorar otras teclas */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita para este caso
    }
}