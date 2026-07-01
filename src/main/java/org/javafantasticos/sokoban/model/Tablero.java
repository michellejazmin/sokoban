package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.muros.Reja;
import org.javafantasticos.sokoban.model.muros.RejaAbierta;
import org.javafantasticos.sokoban.model.muros.RejaCerrada;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Representa el estado actual de un nivel del juego.
 * Mantiene la disposición de la grilla, las cajas, los objetivos y el jugador.
 */

public class Tablero {
    private final String nombre;
    private final List<List<ElementoBase>> grillaInferior;
    private final List<List<ElementoBase>> grillaSuperior;
    private final List<Caja> cajas;
    private final List<Destino> objetivos;
    private final List<Reja> rejas;
    private final Jugador jugador;
    private ElementoBase elementoBajoJugador; // lo que había en la celda antes de que el jugador la pisara
    private BiConsumer<TableroMemento, Integer> onStateChange;
    private Consumer<String> onGameOver;
    private UnaryOperator<ElementoBase> onPisada;
    private Runnable onRejasCambiadas;
    private Suscriptor tableroPanel;
    private final MotorFisico motorFisico = new MotorFisico();

    public Tablero(String nombre,
                   List<List<ElementoBase>> grillaInferior,
                   List<List<ElementoBase>> grillaSuperior,
                   List<Caja> cajas,
                   List<Destino> objetivos,
                   List<Reja> rejas,
                   Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El tablero no tiene jugador");
        }

        this.nombre = nombre;
        this.grillaInferior = grillaInferior;
        this.grillaSuperior = grillaSuperior;
        this.cajas = cajas;
        this.objetivos = List.copyOf(objetivos);
        this.rejas = rejas;
        this.jugador = jugador;
    }

    public String getNombre() {
        return nombre;
    }

    public List<List<ElementoBase>> getGrillaInferior() {
        return grillaInferior;
    }

    public List<List<ElementoBase>> getGrillaSuperior() {
        return grillaSuperior;
    }

    public List<Caja> getCajas() {
        return cajas;
    }

    public List<Destino> getObjetivos() {
        return objetivos;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public ElementoBase getElemento(int posX, int posY) {
        if (grillaSuperior.get(posY).get(posX) != null) {
            return grillaSuperior.get(posY).get(posX);
        }
        return grillaInferior.get(posY).get(posX);
    }

    ElementoBase getElementoBajoJugador() {
        return elementoBajoJugador;
    }

    void setElementoBajoJugador(ElementoBase elemento) {
        this.elementoBajoJugador = elemento;
    }

    public void setOnStateChange(BiConsumer<TableroMemento, Integer> callback) {
        this.onStateChange = callback;
    }

    public void setOnGameOver(Consumer<String> callback) {
        this.onGameOver = callback;
    }

    public void setOnPisada(UnaryOperator<ElementoBase> callback) {
        this.onPisada = callback;
    }

    public void setOnRejasCambiadas(Runnable callback) {
        this.onRejasCambiadas = callback;
    }

    // Package-private accessors for MotorFisico (same package)
    Consumer<String> getOnGameOver() { return onGameOver; }
    UnaryOperator<ElementoBase> getOnPisada() { return onPisada; }
    BiConsumer<TableroMemento, Integer> getOnStateChange() { return onStateChange; }
    Runnable getOnRejasCambiadas() { return onRejasCambiadas; }
    Suscriptor getTableroPanel() { return tableroPanel; }

    public TableroMemento crearMemento() {
        return new TableroMemento(this);
    }

    public void restaurar(TableroMemento memento) {
        memento.restaurar(this);
    }

    public int mover(int dx, int dy) {
        return motorFisico.mover(this, dx, dy);
    }

    public void suscribirVista(Suscriptor tableroPanel) {
        this.tableroPanel = tableroPanel;
    }

    public void desuscribirVista() {
        this.tableroPanel = null;
    }

    void notificarVista() {
        if (tableroPanel != null) {
            tableroPanel.actualizar(this);
        }
    }

    void actualizarRejas() {
        boolean cerrar = !ReglasDelJuego.hayCajaLlaveSobreCerrojo(cajas, objetivos);
        boolean huboCambio = false;

        for (Reja reja : rejas) {
            boolean estabaAbierta = reja.estaAbierta();
            if (cerrar && estabaAbierta) {
                reja.setEstadoReja(new RejaCerrada(reja));
                huboCambio = true;
            } else if (!cerrar && !estabaAbierta) {
                reja.setEstadoReja(new RejaAbierta(reja));
                huboCambio = true;
            }
        }

        if (huboCambio && onRejasCambiadas != null) {
            onRejasCambiadas.run();
        }
    }

    public int getCajasEnDestino() {
        return ReglasDelJuego.contarCajasEnDestino(cajas, objetivos);
    }

}
