package org.javafantasticos.sokoban.controller;

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
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> controller.moverArriba();
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> controller.moverAbajo();
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> controller.moverIzquierda();
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> controller.moverDerecha();
            default  -> { /* Ignorar otras teclas */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita para este caso
    }
}