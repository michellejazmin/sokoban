package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.view.Menu;
import org.javafantasticos.sokoban.view.Ventana;
import org.javafantasticos.sokoban.view.VistaJuego;

/**
 * Orquesta las acciones del juego.
 * No contiene lógica propia, delega al Tablero.
 */
public class GameController {
    private final GestorNiveles gestorNiveles;
    private final Ventana ventana;
    private final Menu vistaMenu;
    private final Caretaker caretaker;
    private VistaJuego vistaJuego;
    private Tablero tablero;
    private Runnable onMove;

    public GameController(GestorNiveles gestorNiveles) {
        this.gestorNiveles = gestorNiveles;
        this.ventana = new Ventana();
        this.vistaMenu = new Menu();
        this.caretaker = new Caretaker();
        this.tablero = gestorNiveles.getTableroActual();
        this.vistaJuego = new VistaJuego(tablero, this);
        this.tablero.suscribirVista(vistaJuego.getTableroPanel());

        tablero.setOnStateChange(memento -> {
            caretaker.saveState(memento);
            if (onMove != null) {
                onMove.run();
            }
        });

        caretaker.saveState(tablero.crearMemento());

        ventana.agregarPantalla(vistaMenu, "MENU");
        ventana.agregarPantalla(vistaJuego, "JUEGO");

        this.vistaMenu.escucharBotonJugar(e -> empezarJuego());
        this.vistaMenu.escucharBotonSalir(e -> System.exit(0));

        ventana.mostrarMenu();
        ventana.setVisible(true);
    }

    public void setOnMove(Runnable callback) {
        this.onMove = callback;
    }

    public void setVistaJuego(VistaJuego vistaJuego) {
        this.vistaJuego = vistaJuego;
    }

    private void empezarJuego() {
        ventana.mostrarJuego();
        MovimientoTeclado teclado = new MovimientoTeclado(this);
        vistaJuego.conectarTeclado(teclado);
        vistaJuego.requestFocusInWindow();
    }

    public void undo() {
        caretaker.undo(tablero);

        if (onMove != null) {
            onMove.run();
        }
    }

    public boolean canUndo() {
        return caretaker.canUndo();
    }

    // TODO: método para avanzar de nivel

    public void moverArriba() {
        tablero.mover(0, -1);
    }

    public void moverAbajo() {
        tablero.mover(0, 1);
    }

    public void moverIzquierda() {
        tablero.mover(-1, 0);
    }

    public void moverDerecha() {
        tablero.mover(1, 0);
    }

    public VistaJuego getVistaJuego() {
        return vistaJuego;
    }

}
