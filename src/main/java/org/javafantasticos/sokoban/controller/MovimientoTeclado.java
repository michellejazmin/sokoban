package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.model.Tablero;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovimientoTeclado implements KeyListener,IMovimientos {
    private final Tablero tablero;
    public MovimientoTeclado(Tablero tablero) {
        this.tablero = tablero;
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
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> derecha();
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> izquierda();
            default  -> { /* Ignorar otras teclas */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita para este caso
    }

    @Override
    public void arriba() {
        tablero.mover(0, -1);
    }

    @Override
    public void abajo() {
        tablero.mover(0, 1);
    }

    @Override
    public void derecha() {
        tablero.mover(-1, 0);
    }

    @Override
    public void izquierda() {
        tablero.mover(1, 0);
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