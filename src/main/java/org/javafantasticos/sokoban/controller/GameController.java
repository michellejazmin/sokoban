package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.items.ContextoItem;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.player.Orientacion;
import org.javafantasticos.sokoban.view.GameOverPanel;
import org.javafantasticos.sokoban.view.HUDPanel;
import org.javafantasticos.sokoban.view.Menu;
import org.javafantasticos.sokoban.view.PasoNivelPanel;
import org.javafantasticos.sokoban.view.ReplayPanel;
import org.javafantasticos.sokoban.view.TableroPanel;
import org.javafantasticos.sokoban.view.Ventana;
import org.javafantasticos.sokoban.view.VictoriaPanel;
import org.javafantasticos.sokoban.view.VistaJuego;

/**
 * Orquesta las acciones del juego.
 * No contiene lógica propia, delega al Tablero.
 * Patrón Singleton: único orquestador de la partida; centraliza la referencia
 * al tablero actual y a las vistas durante toda la ejecución.
 */
public class GameController implements ContextoItem {
    private static GameController instancia;

    private final GestorNiveles gestorNiveles;
    private final Ventana ventana;
    private final Menu vistaMenu;
    private final Caretaker caretaker;
    private final Grabacion grabacion;
    private VistaJuego vistaJuego;
    private HUDPanel hudPanel;
    private Tablero tablero;
    private Jugador jugador;
    private GameOverPanel gameOverPanel;
    private VictoriaPanel victoriaPanel;
    private PasoNivelPanel pasoNivelPanel;
    private ReplayPanel replayPanel;
    private ReproductorPartida reproductor;
    private static final int SCORE_PER_LEVEL = 1000;
    private static final int STEP_PENALTY = 10;
    private static final int PUSH_PENALTY = 15;
    private IMovimientos movimientos;

    private int steps;
    private int pushes;
    private int bonus;
    private boolean partidaTerminada;
    private Runnable onMove;

    private GameController() {
        this.gestorNiveles = GestorNiveles.getInstancia();
        this.ventana = Ventana.getInstancia();
        this.vistaMenu = Menu.getInstancia();
        this.caretaker = new Caretaker();
        this.grabacion = new Grabacion();
        this.tablero = gestorNiveles.getTableroActual();
        this.jugador = tablero.getJugador();
        this.steps = 0;
        this.pushes = 0;
        this.bonus = 0;
        this.partidaTerminada = false;

        this.vistaJuego = VistaJuego.getInstancia(tablero, this);
        this.hudPanel = vistaJuego.getHudPanel();
        this.tablero.suscribirVista(vistaJuego.getTableroPanel());
        this.gameOverPanel = GameOverPanel.getInstancia("");
        this.victoriaPanel = VictoriaPanel.getInstancia("");
        this.pasoNivelPanel = PasoNivelPanel.getInstancia("");
        this.replayPanel = ReplayPanel.getInstancia();

        configurarCallbacksTablero();
        guardarEstadoInicial();

        ventana.agregarPantalla(vistaMenu, "MENU");
        ventana.agregarPantalla(vistaJuego, "JUEGO");
        ventana.agregarPantalla(gameOverPanel, "GAMEOVER");
        ventana.agregarPantalla(victoriaPanel, "VICTORIA");
        ventana.agregarPantalla(pasoNivelPanel, "PASONIVEL");
        ventana.agregarPantalla(replayPanel, "REPLAY");

        this.vistaMenu.escucharBotonJugar(e -> empezarJuego());
        this.vistaMenu.escucharBotonSalir(e -> System.exit(0));
        this.gameOverPanel.escucharBotonReproducir(e -> reproducirPartida());
        this.gameOverPanel.escucharBotonVolver(e -> volverAlMenu());
        this.gameOverPanel.escucharBotonSalir(e -> System.exit(0));
        this.victoriaPanel.escucharBotonReproducir(e -> reproducirPartida());
        this.victoriaPanel.escucharBotonVolver(e -> volverAlMenu());
        this.victoriaPanel.escucharBotonSalir(e -> System.exit(0));
        this.pasoNivelPanel.escucharBotonSiguiente(e -> siguienteNivel());
        this.pasoNivelPanel.escucharBotonVolver(e -> volverAlMenu());
        this.replayPanel.onVolver(e -> volverAlMenu());

        ventana.mostrarMenu();
        ventana.setVisible(true);
    }

    public static GameController getInstancia() {
        if (instancia == null) {
            instancia = new GameController();
        }
        return instancia;
    }

    /** Guarda el estado inicial del tablero tanto en el undo como en la grabación. */
    private void guardarEstadoInicial() {
        var inicial = tablero.crearMemento();
        caretaker.saveState(inicial, steps, pushes);
        grabacion.grabar(inicial, steps, pushes);
    }

    private void configurarCallbacksTablero() {
        tablero.setOnStateChange((memento, pushCount) -> {
            steps++;
            pushes += pushCount;
            caretaker.saveState(memento, steps, pushes);
            grabacion.grabar(memento, steps, pushes);
            if (onMove != null) onMove.run();
            hudPanel.actualizar(this);
            verificarNivelCompletado();
        });
        tablero.setOnGameOver(this::mostrarGameOver);
        tablero.setOnPisada(piso -> piso.aplicar(this));
    }

    public void setOnMove(Runnable callback) {
        this.onMove = callback;
    }

    private void empezarJuego() {
        ventana.mostrarJuego();
        hudPanel.actualizar(this);
        this.movimientos = new MovimientoTeclado(this);
        movimientos.registrarEn(vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    private void mostrarGameOver(String motivo) {
        if (partidaTerminada) return;
        partidaTerminada = true;
        gameOverPanel.setMotivo(motivo);
        ventana.mostrarGameOver();
        if (movimientos != null) movimientos.desregistrarDe(vistaJuego);
        this.movimientos = null;
    }

    private void verificarNivelCompletado() {
        if (partidaTerminada) return;
        if (getTotalCajas() > 0 && getCajasEnDestino() == getTotalCajas()) {
            partidaTerminada = true;
            if (movimientos != null) movimientos.desregistrarDe(vistaJuego);
            this.movimientos = null;

            boolean hayMasNiveles = gestorNiveles.getNivelActualIndex() < gestorNiveles.getTotalNiveles() - 1;
            if (hayMasNiveles) {
                pasoNivelPanel.setMensaje("Nivel " + getNivelActual() + " completado  ·  Puntaje: " + getScore());
                ventana.mostrarPasoNivel();
            } else {
                victoriaPanel.setMensaje("Puntaje: " + getScore()
                        + "  |  Pasos: " + steps + "  |  Mov. cajas: " + pushes);
                ventana.mostrarVictoria();
            }
        }
    }

    /**
     * Reproduce la partida grabada. Como el TableroPanel es único, la pantalla de
     * reproducción lo toma prestado y el reproductor restaura los mementos en orden
     * sobre el mismo Tablero en el que se grabó.
     */
    private void reproducirPartida() {
        if (grabacion.isEmpty()) return;
        if (reproductor != null) reproductor.detener();

        TableroPanel board = vistaJuego.getTableroPanel();
        reproductor = new ReproductorPartida(tablero, grabacion, board);
        replayPanel.cargar(board, reproductor);
        ventana.mostrarReplay();
        reproductor.play();
    }

    public void volverAlMenu() {
        if (reproductor != null) {
            reproductor.detener();
            reproductor = null;
        }
        vistaJuego.recuperarTablero();
        gestorNiveles.reiniciarProgreso();
        tablero = gestorNiveles.getTableroActual();
        recargarTablero();
        if (movimientos != null) movimientos.desregistrarDe(vistaJuego);
        this.movimientos = null;
        ventana.mostrarMenu();
    }

    private void recargarTablero() {
        tablero.suscribirVista(vistaJuego.getTableroPanel());
        configurarCallbacksTablero();
        caretaker.reset();
        grabacion.reset();
        steps = 0;
        pushes = 0;
        bonus = 0;
        partidaTerminada = false;
        guardarEstadoInicial();
        hudPanel.actualizar(this);
        vistaJuego.getTableroPanel().actualizar(tablero);
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
        if (movimientos != null) {
            movimientos.desregistrarDe(vistaJuego);
        }
        tablero = gestorNiveles.reiniciarNivelActual();
        recargarTablero();
        movimientos.registrarEn(vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    public boolean canUndo() {
        return caretaker.canUndo();
    }

    // Movimientos

    public void moverArriba() {
        tablero.mover(0, -1);
        jugador.setOrientacion(Orientacion.ESPALDA);
    }

    public void moverAbajo() {
        tablero.mover(0, 1);
        jugador.setOrientacion(Orientacion.FRENTE);
    }

    public void moverIzquierda() {
        tablero.mover(-1, 0);
        jugador.setOrientacion(Orientacion.IZQUIERDA);
    }

    public void moverDerecha() {
        tablero.mover(1, 0);
        jugador.setOrientacion(Orientacion.DERECHA);
    }

    public Orientacion getOrientacion() {
        return jugador.getOrientacion();
    }

    private void siguienteNivel() {
        gestorNiveles.avanzarNivel();
        tablero = gestorNiveles.getTableroActual();
        recargarTablero();
        ventana.mostrarJuego();
        this.movimientos = new MovimientoTeclado(this);
        movimientos.registrarEn(vistaJuego);
        vistaJuego.requestFocusInWindow();
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
        return Math.max(0, SCORE_PER_LEVEL + bonus - steps * STEP_PENALTY - pushes * PUSH_PENALTY);
    }

    @Override
    public void sumarBonus(int monto) {
        bonus += monto;
        hudPanel.actualizar(this);
    }

    @Override
    public void terminarPartida(String motivo) {
        mostrarGameOver(motivo);
    }

    @Override
    public void sumarUndoExtra() {
        caretaker.agregarUsoUndo();
        hudPanel.actualizar(this);
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
