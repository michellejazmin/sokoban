package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IMovimientos;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovimientoTeclado implements KeyListener,IMovimientos {
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
            case KeyEvent.VK_UP, KeyEvent.VK_W -> arriba();
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> abajo();
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> izquierda();
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> derecha();
            default  -> { /* Ignorar otras teclas */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita para este caso
    }

    @Override
    public void arriba() {
        controller.moverArriba();
    }

    @Override
    public void abajo() {
        controller.moverAbajo();
    }

    @Override
    public void derecha() {
        controller.moverDerecha();
    }

    @Override
    public void izquierda() {
        controller.moverIzquierda();
    }

    @Override
    public void registrarEn(Component componente) {
        componente.addKeyListener(this);
    }

    @Override
    public void desregistrarDe(Component componente) {
        componente.removeKeyListener(this);
    }


}