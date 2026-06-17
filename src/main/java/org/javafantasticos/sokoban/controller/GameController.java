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
    private VistaJuego vistaJuego;
    private Tablero tablero;

    public GameController(GestorNiveles gestorNiveles) {
        this.gestorNiveles = gestorNiveles;
        this.ventana = new Ventana();
        this.vistaMenu = new Menu();
        this.tablero = gestorNiveles.getTableroActual();
        this.vistaJuego = new VistaJuego(tablero);
        this.tablero.suscribirVista(vistaJuego.getTableroPanel());

        ventana.agregarPantalla(vistaMenu, "MENU");
        ventana.agregarPantalla(vistaJuego, "JUEGO");

        this.vistaMenu.escucharBotonJugar(e -> empezarJuego());
        this.vistaMenu.escucharBotonSalir(e -> System.exit(0));

        ventana.mostrarMenu();
        ventana.setVisible(true);
    }

    private void empezarJuego() {
        ventana.mostrarJuego();
        MovimientoTeclado teclado = new MovimientoTeclado(this);
        vistaJuego.conectarTeclado(teclado);
        vistaJuego.requestFocusInWindow();
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
