package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.view.GameOverPanel;
import org.javafantasticos.sokoban.view.HUDPanel;
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
    private HUDPanel hudPanel;
    private Tablero tablero;
    private GameOverPanel gameOverPanel;
    private MovimientoTeclado teclado;
    private static final int SCORE_PER_LEVEL = 1000;
    private static final int STEP_PENALTY = 10;
    private static final int PUSH_PENALTY = 15;

    private int steps;
    private int pushes;
    private Runnable onMove;

    public GameController(GestorNiveles gestorNiveles) {
        this.gestorNiveles = gestorNiveles;
        this.ventana = new Ventana();
        this.vistaMenu = new Menu();
        this.caretaker = new Caretaker();
        this.tablero = gestorNiveles.getTableroActual();
        this.steps = 0;
        this.pushes = 0;

        this.vistaJuego = new VistaJuego(tablero, this);
        this.hudPanel = vistaJuego.getHudPanel();
        this.tablero.suscribirVista(vistaJuego.getTableroPanel());
        this.gameOverPanel = new GameOverPanel("");

        configurarCallbacksTablero();
        caretaker.saveState(tablero.crearMemento(), steps, pushes);

        ventana.agregarPantalla(vistaMenu, "MENU");
        ventana.agregarPantalla(vistaJuego, "JUEGO");
        ventana.agregarPantalla(gameOverPanel, "GAMEOVER");

        this.vistaMenu.escucharBotonJugar(e -> empezarJuego());
        this.vistaMenu.escucharBotonSalir(e -> System.exit(0));
        this.gameOverPanel.escucharBotonVolver(e -> volverAlMenu());
        this.gameOverPanel.escucharBotonSalir(e -> System.exit(0));

        ventana.mostrarMenu();
        ventana.setVisible(true);
    }

    private void configurarCallbacksTablero() {
        tablero.setOnStateChange((memento, pushCount) -> {
            steps++;
            pushes += pushCount;
            caretaker.saveState(memento, steps, pushes);
            if (onMove != null) onMove.run();
            hudPanel.actualizar(this);
        });
        tablero.setOnGameOver(reason -> mostrarGameOver(reason));
    }

    public void setOnMove(Runnable callback) {
        this.onMove = callback;
    }

    private void empezarJuego() {
        ventana.mostrarJuego();
        hudPanel.actualizar(this);
        teclado = new MovimientoTeclado(this);
        vistaJuego.conectarTeclado(teclado);
        vistaJuego.requestFocusInWindow();
    }

    private void mostrarGameOver(String motivo) {
        gameOverPanel.setMotivo(motivo);
        ventana.mostrarGameOver();
        teclado = null;
    }

    public void volverAlMenu() {
        gestorNiveles.reiniciarProgreso();
        tablero = gestorNiveles.getTableroActual();
        tablero.suscribirVista(vistaJuego.getTableroPanel());
        configurarCallbacksTablero();
        caretaker.reset();
        steps = 0;
        pushes = 0;
        caretaker.saveState(tablero.crearMemento(), steps, pushes);
        hudPanel.actualizar(this);
        vistaJuego.getTableroPanel().actualizar(tablero);
        teclado = null;
        ventana.mostrarMenu();
    }

    public void undo() {
        Caretaker.Snapshot snap = caretaker.undo(tablero);
        if (snap != null) {
            steps = snap.steps();
            pushes = snap.pushes();
            if (onMove != null) onMove.run();
            hudPanel.actualizar(this);
        }
    }

    public void reiniciarNivel() {
        tablero = gestorNiveles.reiniciarNivelActual();
        tablero.suscribirVista(vistaJuego.getTableroPanel());
        configurarCallbacksTablero();
        caretaker.reset();
        steps = 0;
        pushes = 0;
        caretaker.saveState(tablero.crearMemento(), steps, pushes);
        hudPanel.actualizar(this);
        vistaJuego.getTableroPanel().actualizar(tablero);
        vistaJuego.requestFocusInWindow();
    }

    public boolean canUndo() {
        return caretaker.canUndo();
    }

    // TODO: método para avanzar de nivel
    // Movimiento

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

    // Getters para el HUD

    public int getSteps() {
        return steps;
    }

    public int getPushes() {
        return pushes;
    }

    public int getNivelActual() {
        return gestorNiveles.getNivelActualIndex() + 1;
    }

    public int getTotalNiveles() {
        return gestorNiveles.getTotalNiveles();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getCajasEnDestino() {
        return tablero.getCajasEnDestino();
    }

    public int getTotalCajas() {
        return tablero.getCajas().size();
    }

    public int getScore() {
        return Math.max(0, SCORE_PER_LEVEL - steps * STEP_PENALTY - pushes * PUSH_PENALTY);
    }

    public int getUndoRemaining() {
        return caretaker.getRemainingUndos();
    }

    public int getUndoStepSize() {
        return caretaker.getUndoStepSize();
    }

    public int getMaxUndoUses() {
        return caretaker.getMaxUndoUses();
    }

    public VistaJuego getVistaJuego() {
        return vistaJuego;
    }
}
