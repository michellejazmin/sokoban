package org.example.model.player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovimientoTeclado implements KeyListener {
    private final Jugador jugador;

    public MovimientoTeclado(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita para este caso
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (jugador == null) {
            return;
        }


        switch (Character.toLowerCase(e.getKeyChar())) {
            case 'w' -> jugador.arriba();
            case 'a' -> jugador.izquierda();
            case 's' -> jugador.abajo();
            case 'd' -> jugador.derecha();
            default  -> { /* Ignorar otras teclas */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita para este caso
    }
}