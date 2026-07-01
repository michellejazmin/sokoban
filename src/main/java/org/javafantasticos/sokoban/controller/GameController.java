package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.ControladorVista;
import org.javafantasticos.sokoban.interfaces.IMovimientos;
import org.javafantasticos.sokoban.interfaces.NavegadorPantallas;
import org.javafantasticos.sokoban.interfaces.PantallaGameOver;
import org.javafantasticos.sokoban.interfaces.PantallaPasoNivel;
import org.javafantasticos.sokoban.interfaces.PantallaVictoria;
import org.javafantasticos.sokoban.interfaces.ReproductorSonido;
import org.javafantasticos.sokoban.interfaces.VistaDeJuego;
import org.javafantasticos.sokoban.interfaces.VistaHUD;
import org.javafantasticos.sokoban.interfaces.VistaMenu;
import org.javafantasticos.sokoban.interfaces.VistaReplay;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.items.ContextoItem;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.player.Orientacion;
import org.javafantasticos.sokoban.view.GameOverPanel;
import org.javafantasticos.sokoban.view.Menu;
import org.javafantasticos.sokoban.view.PasoNivelPanel;
import org.javafantasticos.sokoban.view.ReplayPanel;
import org.javafantasticos.sokoban.view.Ventana;
import org.javafantasticos.sokoban.view.VictoriaPanel;
import org.javafantasticos.sokoban.view.VistaJuego;

public class GameController implements ContextoItem, ControladorVista {
    private static GameController instancia;

    private final GestorNiveles gestorNiveles;
    private final GestorDePartida gestorDePartida;
    private final NavegadorPantallas navegador;
    private final VistaMenu vistaMenu;
    private VistaDeJuego vistaJuego;
    private VistaHUD hudPanel;
    private Tablero tablero;
    private Tablero tableroParaReplay;
    private Jugador jugador;
    private PantallaGameOver gameOverPanel;
    private PantallaVictoria victoriaPanel;
    private PantallaPasoNivel pasoNivelPanel;
    private VistaReplay replayPanel;
    private ReproductorPartida reproductor;
    private IMovimientos movimientos;
    private ReproductorSonido reproductorSonido;

    private Runnable onMove;

    private GameController() {
        this.gestorNiveles = GestorNiveles.getInstancia();
        this.gestorDePartida = new GestorDePartida();
        this.navegador = Ventana.getInstancia();
        this.vistaMenu = Menu.getInstancia();
        this.tablero = gestorNiveles.getTableroActual();
        this.jugador = tablero.getJugador();

        this.vistaJuego = new VistaJuego(tablero, this);
        this.hudPanel = vistaJuego.getHudPanel();
        this.tablero.suscribirVista(vistaJuego.getTableroPanel());
        this.reproductorSonido = GestorSonido.getInstancia();
        this.gameOverPanel = GameOverPanel.getInstancia("");
        this.victoriaPanel = VictoriaPanel.getInstancia("");
        this.pasoNivelPanel = PasoNivelPanel.getInstancia("");
        this.replayPanel = ReplayPanel.getInstancia();

        configurarCallbacksTablero();
        gestorDePartida.guardarInicial(tablero);

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

    public static GameController getInstancia() {
        if (instancia == null) {
            instancia = new GameController();
        }
        return instancia;
    }

    private void configurarCallbacksTablero() {
        tablero.setOnRejasCambiadas(() -> reproductorSonido.reproducir(ReproductorSonido.REJA));
        tablero.setOnStateChange((memento, pushCount) -> {
            gestorDePartida.registrarMovimiento(memento, pushCount);
            if (onMove != null) onMove.run();
            hudPanel.actualizar(this);
            if (pushCount > 0) {
                reproductorSonido.reproducir(ReproductorSonido.EMPUJE);
            } else {
                reproductorSonido.reproducir(ReproductorSonido.MOVIMIENTO);
            }
            verificarNivelCompletado();
        });
        tablero.setOnGameOver(motivo -> {
            reproductorSonido.reproducir(ReproductorSonido.CAJA_ROTA);
            mostrarGameOver(motivo);
        });
        tablero.setOnPisada(piso -> piso.aplicar(this));
    }

    public void setOnMove(Runnable callback) {
        this.onMove = callback;
    }

    private void empezarJuego() {
        navegador.mostrarJuego();
        hudPanel.actualizar(this);
        this.movimientos = new MovimientoTeclado(this);
        movimientos.registrarEn((java.awt.Component) vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    private void mostrarGameOver(String motivo) {
        if (gestorDePartida.isPartidaTerminada()) return;
        gestorDePartida.setPartidaTerminada(true);
        reproductorSonido.reproducir(ReproductorSonido.GAME_OVER);
        gameOverPanel.setMotivo(motivo);
        navegador.mostrarGameOver();
        if (movimientos != null) movimientos.desregistrarDe((java.awt.Component) vistaJuego);
        this.movimientos = null;
    }

    private void verificarNivelCompletado() {
        if (gestorDePartida.isPartidaTerminada()) return;
        if (getTotalCajas() > 0 && getCajasEnDestino() == getTotalCajas()) {
            gestorDePartida.setPartidaTerminada(true);
            if (movimientos != null) movimientos.desregistrarDe((java.awt.Component) vistaJuego);
            this.movimientos = null;

            boolean hayMasNiveles = gestorNiveles.getNivelActualIndex() < gestorNiveles.getTotalNiveles() - 1;
            if (hayMasNiveles) {
                reproductorSonido.reproducir(ReproductorSonido.PASO_NIVEL);
                pasoNivelPanel.setMensaje("Nivel " + getNivelActual() + " completado  ·  Puntaje: " + getScore());
                navegador.mostrarPasoNivel();
            } else {
                reproductorSonido.reproducir(ReproductorSonido.VICTORIA);
                victoriaPanel.setMensaje("Puntaje: " + getScore()
                        + "  |  Pasos: " + getSteps() + "  |  Mov. cajas: " + getPushes());
                navegador.mostrarVictoria();
            }
        }
    }

    /**
     * Reproduce la partida grabada. Como el TableroPanel es único, la pantalla de
     * reproducción lo toma prestado y el reproductor restaura los mementos en orden
     * sobre el mismo Tablero en el que se grabó.
     * @param mostrarContinuar true muestra "Siguiente nivel" (paso de nivel),
     *                         false muestra "Volver al menú" (game over / final).
     */
    private void reproducirPartida(boolean mostrarContinuar) {
        if (gestorDePartida.isGrabacionVacia()) return;
        if (reproductor != null) reproductor.detener();

        Tablero tableroReplay = (tableroParaReplay != null) ? tableroParaReplay : tablero;
        reproductor = new ReproductorPartida(tableroReplay, gestorDePartida.getGrabacion(), vistaJuego.getTableroPanel());
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
        vistaJuego.recuperarTablero();
        gestorNiveles.reiniciarProgreso();
        tablero = gestorNiveles.getTableroActual();
        sincronizarJugador();
        recargarTablero();
        if (movimientos != null) movimientos.desregistrarDe((java.awt.Component) vistaJuego);
        this.movimientos = null;
        navegador.mostrarMenu();
    }

    private void recargarTablero() {
        tableroParaReplay = null;
        tablero.suscribirVista(vistaJuego.getTableroPanel());
        configurarCallbacksTablero();
        gestorDePartida.reiniciar();
        sincronizarJugador();
        gestorDePartida.guardarInicial(tablero);
        hudPanel.actualizar(this);
        vistaJuego.getTableroPanel().actualizar(tablero);
    }

    public void undo() {
        if (gestorDePartida.undo(tablero)) {
            if (onMove != null) onMove.run();
            hudPanel.actualizar(this);
            reproductorSonido.reproducir(ReproductorSonido.UNDO);
        }
    }

    public void reiniciarNivel() {
        jugador.setOrientacion(Orientacion.FRENTE);
        tablero = gestorNiveles.reiniciarNivelActual();
        if (movimientos != null) {
            movimientos.desregistrarDe((java.awt.Component) vistaJuego);
        }
        recargarTablero();
        if (movimientos == null) {
            movimientos = new MovimientoTeclado(this);
        }
        movimientos.registrarEn((java.awt.Component) vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    public boolean canUndo() {
        return gestorDePartida.canUndo();
    }

    // Movimientos

    public void moverArriba() {
        jugador.setOrientacion(Orientacion.ESPALDA);
        tablero.mover(0, -1);
    }

    public void moverAbajo() {
        jugador.setOrientacion(Orientacion.FRENTE);
        tablero.mover(0, 1);
    }

    public void moverIzquierda() {
        jugador.setOrientacion(Orientacion.IZQUIERDA);
        tablero.mover(-1, 0);
    }

    public void moverDerecha() {
        jugador.setOrientacion(Orientacion.DERECHA);
        tablero.mover(1, 0);
    }

    public Orientacion getOrientacion() {
        return jugador.getOrientacion();
    }

    public void sincronizarJugador() {
        this.jugador = tablero.getJugador();
    }

    private void siguienteNivel() {
        vistaJuego.recuperarTablero();
        gestorNiveles.avanzarNivel();
        tablero = gestorNiveles.getTableroActual();
        sincronizarJugador();
        jugador.setOrientacion(Orientacion.FRENTE);
        recargarTablero();
        navegador.mostrarJuego();
        this.movimientos = new MovimientoTeclado(this);
        movimientos.registrarEn((java.awt.Component) vistaJuego);
        vistaJuego.requestFocusInWindow();
    }

    // Getters para el HUD

    public int getSteps() {
        return gestorDePartida.getSteps();
    }

    public int getPushes() {
        return gestorDePartida.getPushes();
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
        return gestorDePartida.getScore();
    }

    @Override
    public void sumarBonus(int monto) {
        gestorDePartida.sumarBonus(monto);
        reproductorSonido.reproducir(ReproductorSonido.MONEDA);
        hudPanel.actualizar(this);
    }

    @Override
    public void terminarPartida(String motivo) {
        reproductorSonido.reproducir(ReproductorSonido.BOMBA);
        if (reproductor != null) {
            reproductor.detener();
            reproductor = null;
        }
        vistaJuego.recuperarTablero();
        tableroParaReplay = tablero;
        gestorNiveles.reiniciarProgreso();
        tablero = gestorNiveles.getTableroActual();
        if (movimientos != null) movimientos.desregistrarDe((java.awt.Component) vistaJuego);
        this.movimientos = null;
        mostrarGameOver(motivo);
    }

    @Override
    public void sumarUndoExtra() {
        gestorDePartida.sumarUndoExtra();
        reproductorSonido.reproducir(ReproductorSonido.UNDO_ITEM);
        hudPanel.actualizar(this);
    }

    public int getUndoRemaining() {
        return gestorDePartida.getUndoRemaining();
    }

    public int getUndoStepSize() {
        return gestorDePartida.getUndoStepSize();
    }

    public int getMaxUndoUses() {
        return gestorDePartida.getMaxUndoUses();
    }

}
