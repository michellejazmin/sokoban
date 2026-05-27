package org.example.model.player;

<<<<<<< HEAD
=======
import org.example.controller.GameController;

>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovimientoTeclado implements KeyListener {
<<<<<<< HEAD
    private final Jugador jugador;

    public MovimientoTeclado(Jugador jugador) {
        this.jugador = jugador;
=======
    private final GameController controller;

    public MovimientoTeclado(GameController controller) {
        this.controller = controller;
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No se necesita para este caso
    }

    @Override
    public void keyPressed(KeyEvent e) {
<<<<<<< HEAD
        if (jugador == null) {
            return;
        }


        switch (Character.toLowerCase(e.getKeyChar())) {
            case 'w' -> jugador.arriba();
            case 'a' -> jugador.izquierda();
            case 's' -> jugador.abajo();
            case 'd' -> jugador.derecha();
=======
        switch (Character.toLowerCase(e.getKeyChar())) {
            case 'w' -> controller.moverArriba();
            case 'a' -> controller.moverIzquierda();
            case 's' -> controller.moverAbajo();
            case 'd' -> controller.moverDerecha();
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
            default  -> { /* Ignorar otras teclas */ }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No se necesita para este caso
    }
}