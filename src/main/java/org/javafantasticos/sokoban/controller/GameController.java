package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.MoveCallback;
import org.javafantasticos.sokoban.interfaces.NavegadorPantallas;
import org.javafantasticos.sokoban.interfaces.PantallaGameOver;
import org.javafantasticos.sokoban.interfaces.PantallaPasoNivel;
import org.javafantasticos.sokoban.interfaces.PantallaVictoria;
import org.javafantasticos.sokoban.interfaces.ReproductorSonido;
import org.javafantasticos.sokoban.interfaces.VistaMenu;
import org.javafantasticos.sokoban.interfaces.VistaReplay;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.interfaces.ContextoItem;
import org.javafantasticos.sokoban.view.GameOverPanel;
import org.javafantasticos.sokoban.view.Menu;
import org.javafantasticos.sokoban.view.PasoNivelPanel;
import org.javafantasticos.sokoban.view.ReplayPanel;
import org.javafantasticos.sokoban.view.Ventana;
import org.javafantasticos.sokoban.view.VictoriaPanel;
import org.javafantasticos.sokoban.view.VistaJuego;

public class GameController implements ContextoItem, MoveCallback {
    private static GameController instancia;

    private final InputController inputController;
    private final GameFlowController flowController;
    private final NavegadorPantallas navegador;
    private final VistaMenu vistaMenu;
    private final VistaJuego vistaJuego;
    private Tablero tableroParaReplay;
    private PantallaGameOver gameOverPanel;
    private PantallaVictoria victoriaPanel;
    private PantallaPasoNivel pasoNivelPanel;
    private VistaReplay replayPanel;
    private ReproductorPartida reproductor;
    private ReproductorSonido reproductorSonido;
    private Runnable onMove;

    private GameController() {
        GestorNiveles gestorNiveles = GestorNiveles.getInstancia();
        this.navegador = Ventana.getInstancia();
        this.vistaMenu = Menu.getInstancia();

        Tablero tableroInicial = gestorNiveles.getTableroActual();
        this.vistaJuego = new VistaJuego(tableroInicial, this::undo, this::reiniciarNivel,
                this::volverAlMenu, this);
        this.flowController = new GameFlowController(gestorNiveles, navegador, vistaJuego);
        this.inputController = new InputController(
                flowController::getTablero, flowController::getJugador, this::undo);

        this.reproductorSonido = GestorSonido.getInstancia();
        this.gameOverPanel = GameOverPanel.getInstancia("");
        this.victoriaPanel = VictoriaPanel.getInstancia("");
        this.pasoNivelPanel = PasoNivelPanel.getInstancia("");
        this.replayPanel = ReplayPanel.getInstancia();

        configurarCallbacks();
        gestorDePartida().guardarInicial(flowController.getTablero());

        navegador.agregarPantalla((javax.swing.JPanel) vistaMenu, "MENU");
        navegador.agregarPantalla((javax.swing.JPanel) vistaJuego, "JUEGO");
        navegador.agregarPantalla((javax.swing.JPanel) gameOverPanel, "GAMEOVER");
        navegador.agregarPantalla((javax.swing.JPanel) victoriaPanel, "VICTORIA");
        navegador.agregarPantalla((javax.swing.JPanel) pasoNivelPanel, "PASONIVEL");
        navegador.agregarPantalla((javax.swing.JPanel) replayPanel, "REPLAY");

        this.vistaMenu.escucharBotonJugar(e -> empezarJuego());
        this.vistaMenu.escucharBotonSalir(e -> System.exit(0));
        this.gameOverPanel.escucharBotonReproducir(e -> reproducirPartida(false));
        this.gameOverPanel.escucharBotonVolver(e -> volverAlMenu());
        this.gameOverPanel.escucharBotonSalir(e -> System.exit(0));
        this.victoriaPanel.escucharBotonReproducir(e -> reproducirPartida(false));
        this.victoriaPanel.escucharBotonVolver(e -> volverAlMenu());
        this.victoriaPanel.escucharBotonSalir(e -> System.exit(0));
        this.pasoNivelPanel.escucharBotonSiguiente(e -> siguienteNivel());
        this.pasoNivelPanel.escucharBotonReproducir(e -> reproducirPartida(true));
        this.pasoNivelPanel.escucharBotonVolver(e -> volverAlMenu());

        navegador.mostrarMenu();
        navegador.setVisible(true);
    }

    private GestorDePartida gestorDePartida() {
        return flowController.getGestorDePartida();
    }

    private void configurarCallbacks() {
        flowController.configurarCallbacks(
            () -> reproductorSonido.reproducir(ReproductorSonido.REJA),
            (pushCount, tablero) -> {
                if (onMove != null) onMove.run();
                if (pushCount > 0) {
                    reproductorSonido.reproducir(ReproductorSonido.EMPUJE);
                } else {
                    reproductorSonido.reproducir(ReproductorSonido.MOVIMIENTO);
                }
            },
            motivo -> {
                reproductorSonido.reproducir(ReproductorSonido.CAJA_ROTA);
                mostrarGameOver(motivo);
            },
            piso -> piso.aplicar(this),
            () -> {
                inputController.desregistrar(vistaJuego);
                boolean hayMasNiveles = GestorNiveles.getInstancia().getNivelActualIndex()
                        < GestorNiveles.getInstancia().getTotalNiveles() - 1;
                if (hayMasNiveles) {
                    reproductorSonido.reproducir(ReproductorSonido.PASO_NIVEL);
                    pasoNivelPanel.setMensaje("Nivel " + flowController.getNivelActual()
                            + " completado  ·  Puntaje: " + flowController.getScore());
                } else {
                    reproductorSonido.reproducir(ReproductorSonido.VICTORIA);
                    victoriaPanel.setMensaje("Puntaje: " + flowController.getScore()
                            + "  |  Pasos: " + flowController.getSteps()
                            + "  |  Mov. cajas: " + flowController.getPushes());
                }
            }
        );
    }

    public static GameController getInstancia() {
        if (instancia == null) {
            instancia = new GameController();
        }
        return instancia;
    }

    // ── MoveCallback ──

    @Override
    public void setOnMove(Runnable callback) {
        this.onMove = callback;
    }

    // ── Game flow ──

    private void empezarJuego() {
        flowController.empezarJuego();
        inputController.registrar(vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    private void mostrarGameOver(String motivo) {
        if (flowController.isPartidaTerminada()) return;
        reproductorSonido.reproducir(ReproductorSonido.GAME_OVER);
        flowController.mostrarGameOver(msg -> gameOverPanel.setMotivo(msg), motivo);
        inputController.desregistrar(vistaJuego);
    }

    // ── Undo ──

    public void undo() {
        flowController.undo();
        if (onMove != null) onMove.run();
        reproductorSonido.reproducir(ReproductorSonido.UNDO);
    }

    // ── Replay ──

    private void reproducirPartida(boolean mostrarContinuar) {
        if (gestorDePartida().isGrabacionVacia()) return;
        if (reproductor != null) reproductor.detener();

        Tablero tableroReplay = (tableroParaReplay != null) ? tableroParaReplay : flowController.getTablero();
        reproductor = new ReproductorPartida(tableroReplay, gestorDePartida().getGrabacion(),
                vistaJuego.getTableroPanel());
        replayPanel.cargar(vistaJuego.getTableroPanel(), reproductor, mostrarContinuar,
                e -> volverAlMenu(), e -> siguienteNivel());
        navegador.mostrarReplay();
        reproductor.play();
    }

    public void volverAlMenu() {
        if (reproductor != null) {
            reproductor.detener();
            reproductor = null;
        }
        tableroParaReplay = null;
        flowController.volverAlMenu();
        inputController.desregistrar(vistaJuego);
    }

    public void reiniciarNivel() {
        flowController.reiniciarNivel();
        inputController.registrar(vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    private void siguienteNivel() {
        flowController.siguienteNivel();
        inputController.registrar(vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    // ── ContextoItem ──

    @Override
    public void sumarBonus(int monto) {
        gestorDePartida().sumarBonus(monto);
        reproductorSonido.reproducir(ReproductorSonido.MONEDA);
        vistaJuego.getHudPanel().actualizar(flowController);
    }

    @Override
    public void terminarPartida(String motivo) {
        reproductorSonido.reproducir(ReproductorSonido.BOMBA);
        if (reproductor != null) {
            reproductor.detener();
            reproductor = null;
        }
        tableroParaReplay = flowController.getTablero();
        flowController.getTablero().suscribirVista(vistaJuego.getTableroPanel());
        flowController.reiniciarProgreso();
        inputController.desregistrar(vistaJuego);
        mostrarGameOver(motivo);
    }

    @Override
    public void sumarUndoExtra() {
        gestorDePartida().sumarUndoExtra();
        reproductorSonido.reproducir(ReproductorSonido.UNDO_ITEM);
        vistaJuego.getHudPanel().actualizar(flowController);
    }

    // ── Getters ──

    public boolean canUndo() { return flowController.canUndo(); }
    public int getSteps() { return flowController.getSteps(); }
    public int getPushes() { return flowController.getPushes(); }
    public int getNivelActual() { return flowController.getNivelActual(); }
    public int getTotalNiveles() { return flowController.getTotalNiveles(); }
    public Tablero getTablero() { return flowController.getTablero(); }
    public int getCajasEnDestino() { return flowController.getCajasEnDestino(); }
    public int getTotalCajas() { return flowController.getTotalCajas(); }
    public int getScore() { return flowController.getScore(); }
    public int getUndoRemaining() { return flowController.getUndoRemaining(); }
    public int getUndoStepSize() { return flowController.getUndoStepSize(); }
    public int getMaxUndoUses() { return flowController.getMaxUndoUses(); }
}
