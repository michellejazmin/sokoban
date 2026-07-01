package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IHUDDataSource;
import org.javafantasticos.sokoban.interfaces.INavegadorPantallas;
import org.javafantasticos.sokoban.interfaces.IVistaDeJuego;
import org.javafantasticos.sokoban.interfaces.IVistaHUD;
import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.player.Orientacion;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class GameFlowController implements IHUDDataSource {
    private final GestorNiveles gestorNiveles;
    private final GestorDePartida gestorDePartida;
    private final INavegadorPantallas navegador;
    private final IVistaDeJuego vistaJuego;
    private final IVistaHUD hudPanel;

    private Tablero tablero;
    private Jugador jugador;

    public GameFlowController(GestorNiveles gestorNiveles, INavegadorPantallas navegador,
                              IVistaDeJuego vistaJuego) {
        this.gestorNiveles = gestorNiveles;
        this.gestorDePartida = new GestorDePartida();
        this.navegador = navegador;
        this.vistaJuego = vistaJuego;
        this.hudPanel = vistaJuego.getHudPanel();
        this.tablero = gestorNiveles.getTableroActual();
        this.jugador = tablero.getJugador();
    }

    public Tablero getTablero() { return tablero; }
    public Jugador getJugador() { return jugador; }
    public GestorDePartida getGestorDePartida() { return gestorDePartida; }

    public void sincronizarJugador() {
        this.jugador = tablero.getJugador();
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
        reconfigurarCallbacks();
    }

    public void empezarJuego() {
        navegador.mostrarJuego();
        reconfigurarCallbacks();
        hudPanel.actualizar(this);
    }

    public void undo() {
        if (gestorDePartida.undo(tablero)) {
            hudPanel.actualizar(this);
        }
    }

    public boolean canUndo() {
        return gestorDePartida.canUndo();
    }

    public void reiniciarNivel() {
        jugador.setOrientacion(Orientacion.FRENTE);
        tablero = gestorNiveles.reiniciarNivelActual();
        sincronizarJugador();
        recargarConSubscripcion();
    }

    public void volverAlMenu() {
        vistaJuego.recuperarTablero();
        gestorNiveles.reiniciarProgreso();
        tablero = gestorNiveles.getTableroActual();
        sincronizarJugador();
        recargarConSubscripcion();
        navegador.mostrarMenu();
    }

    public void siguienteNivel() {
        vistaJuego.recuperarTablero();
        gestorNiveles.avanzarNivel();
        tablero = gestorNiveles.getTableroActual();
        sincronizarJugador();
        jugador.setOrientacion(Orientacion.FRENTE);
        recargarConSubscripcion();
        navegador.mostrarJuego();
    }

    private void recargar() {
        sincronizarJugador();
        gestorDePartida.reiniciar();
        gestorDePartida.guardarInicial(tablero);
        hudPanel.actualizar(this);
    }

    public void recargarConSubscripcion() {
        tablero.suscribirVista(vistaJuego.getTableroPanel());
        reconfigurarCallbacks();
        recargar();
        vistaJuego.getTableroPanel().actualizar(tablero);
    }

    private Runnable onRejasCambiadas;
    private BiConsumer<Integer, Tablero> onMove;
    private Consumer<String> onGameOver;
    private UnaryOperator<ElementoBase> onPisada;
    private Runnable onLevelComplete;

    public void configurarCallbacks(Runnable onRejasCambiadas,
                                     BiConsumer<Integer, Tablero> onMove,
                                     Consumer<String> onGameOver,
                                     UnaryOperator<ElementoBase> onPisada,
                                     Runnable onLevelComplete) {
        this.onRejasCambiadas = onRejasCambiadas;
        this.onMove = onMove;
        this.onGameOver = onGameOver;
        this.onPisada = onPisada;
        this.onLevelComplete = onLevelComplete;
        reconfigurarCallbacks();
    }

    private void reconfigurarCallbacks() {
        tablero.setOnRejasCambiadas(onRejasCambiadas);
        tablero.setOnStateChange((memento, pushCount) -> {
            gestorDePartida.registrarMovimiento(memento, pushCount);
            onMove.accept(pushCount, tablero);
            hudPanel.actualizar(this);
            verificarNivelCompletado();
        });
        tablero.setOnGameOver(motivo -> onGameOver.accept(motivo));
        tablero.setOnPisada(onPisada);
    }

    public boolean isPartidaTerminada() {
        return gestorDePartida.isPartidaTerminada();
    }

    public void setPartidaTerminada(boolean terminada) {
        gestorDePartida.setPartidaTerminada(terminada);
    }

    public void mostrarGameOver(Consumer<String> mostrarPanel, String motivo) {
        if (gestorDePartida.isPartidaTerminada()) return;
        gestorDePartida.setPartidaTerminada(true);
        mostrarPanel.accept(motivo);
        navegador.mostrarGameOver();
    }

    public void verificarNivelCompletado() {
        if (gestorDePartida.isPartidaTerminada()) return;
        if (getTotalCajas() > 0 && getCajasEnDestino() == getTotalCajas()) {
            gestorDePartida.setPartidaTerminada(true);
            if (onLevelComplete != null) onLevelComplete.run();
            boolean hayMasNiveles = gestorNiveles.getNivelActualIndex() < gestorNiveles.getTotalNiveles() - 1;
            if (hayMasNiveles) {
                navegador.mostrarPasoNivel();
            } else {
                navegador.mostrarVictoria();
            }
        }
    }

    public void reiniciarProgreso() {
        vistaJuego.recuperarTablero();
        gestorNiveles.reiniciarProgreso();
        tablero = gestorNiveles.getTableroActual();
        sincronizarJugador();
        reconfigurarCallbacks();
    }

    // ── HUDDataSource ──

    @Override public int getScore() { return gestorDePartida.getScore(); }
    @Override public int getSteps() { return gestorDePartida.getSteps(); }
    @Override public int getPushes() { return gestorDePartida.getPushes(); }
    @Override public int getNivelActual() { return gestorNiveles.getNivelActualIndex() + 1; }
    @Override public int getTotalNiveles() { return gestorNiveles.getTotalNiveles(); }
    @Override public int getCajasEnDestino() { return tablero.getCajasEnDestino(); }
    @Override public int getTotalCajas() { return tablero.getCajas().size(); }
    @Override public int getUndoRemaining() { return gestorDePartida.getUndoRemaining(); }
    @Override public int getUndoStepSize() { return gestorDePartida.getUndoStepSize(); }
    @Override public int getMaxUndoUses() { return gestorDePartida.getMaxUndoUses(); }
}
