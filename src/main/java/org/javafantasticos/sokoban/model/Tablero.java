package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.enumerados.Estado;
import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.view.TableroPanel;

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
    private final Jugador jugador;
    private ElementoBase elementoBajoJugador; // lo que había en la celda antes de que el jugador la pisara
    private BiConsumer<TableroMemento, Integer> onStateChange;
    private Consumer<String> onGameOver;
    private UnaryOperator<ElementoBase> onPisada;
    private Suscriptor tableroPanel;

    public Tablero(String nombre,
                   List<List<ElementoBase>> grillaInferior,
                   List<List<ElementoBase>> grillaSuperior,
                   List<Caja> cajas,
                   List<Destino> objetivos,
                   Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El tablero no tiene jugador");
        }

        this.nombre = nombre;
        this.grillaInferior = grillaInferior;
        this.grillaSuperior = grillaSuperior;
        this.cajas = cajas;
        this.objetivos = List.copyOf(objetivos);
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

    public TableroMemento crearMemento() {
        return new TableroMemento(this);
    }

    public void restaurar(TableroMemento memento) {
        memento.restaurar(this);
    }

    /**
     * @return cantidad de cajas empujadas en este paso (incluyendo deslizamientos por aceite)
     */
    public int mover(int dx, int dy) {
        int xJugador = jugador.getCoordenada().getPosX();
        int yJugador = jugador.getCoordenada().getPosY();
        int xDestinoJugador = xJugador + dx;
        int yDestinoJugador = yJugador + dy;

        ElementoBase elementoAdyacente = getElemento(xDestinoJugador, yDestinoJugador);

        if (elementoAdyacente.bloqueaPaso()) return 0;

        boolean pushEnEstePaso = false;
        int xDestinoCaja = -1;
        int yDestinoCaja = -1;

        // Si el elemento adyacente es una caja, intentamos moverla
        if (elementoAdyacente.esMovible()) {
            xDestinoCaja = xDestinoJugador + dx;
            yDestinoCaja = yDestinoJugador + dy;
            ElementoBase destinoCaja = getElemento(xDestinoCaja, yDestinoCaja);

            // Si lo que hay detrás de la caja bloquea, no podemos mover
            if (!destinoCaja.esOcupable()) return 0;

            elementoAdyacente.getCoordenada().setPosX(xDestinoCaja);
            elementoAdyacente.getCoordenada().setPosY(yDestinoCaja);
            grillaSuperior.get(yDestinoCaja).set(xDestinoCaja, elementoAdyacente);

            pushEnEstePaso = true;
        }

        jugador.getCoordenada().setPosX(xDestinoJugador);
        jugador.getCoordenada().setPosY(yDestinoJugador);
        grillaSuperior.get(yJugador).set(xJugador, null);
        grillaSuperior.get(yDestinoJugador).set(xDestinoJugador, jugador);

        if (onPisada != null) {
            ElementoBase pisoActual = grillaInferior.get(yDestinoJugador).get(xDestinoJugador);
            ElementoBase nuevoPiso = onPisada.apply(pisoActual);
            if (nuevoPiso != pisoActual) {
                grillaInferior.get(yDestinoJugador).set(xDestinoJugador, nuevoPiso);
            }
        }

        int pushes = 0;
        if (pushEnEstePaso) {
            pushes = 1;

            elementoAdyacente.alSerEmpujada(onGameOver);

            // Deslizamiento de la caja sobre aceite
            ElementoBase terrenoCaja = grillaInferior.get(yDestinoCaja).get(xDestinoCaja);
            if (terrenoCaja.esResbaloso()) {
                pushes += deslizarCaja(elementoAdyacente, dx, dy);
            }
        }

        actualizarRejas();

        if (onStateChange != null) {
            onStateChange.accept(new TableroMemento(this), pushes);
        }

        // Deslizamiento del jugador sobre aceite
        if (elementoAdyacente.esResbaloso()) {
            pushes += mover(dx, dy);
        }

        notificarVista();
        return pushes;
    }

    /**
     * Desliza una caja sobre terreno resbaladizo (aceite) hasta que encuentre
     * un obstáculo o llegue a una casilla no resbaladiza.
     * @return cantidad de casillas que la caja se deslizó
     */
    private int deslizarCaja(ElementoBase caja, int dx, int dy) {
        int xActual = caja.getCoordenada().getPosX();
        int yActual = caja.getCoordenada().getPosY();
        int xSiguiente = xActual + dx;
        int ySiguiente = yActual + dy;

        ElementoBase destino = getElemento(xSiguiente, ySiguiente);

        if (!destino.esOcupable()) {
            return 0;
        }

        caja.getCoordenada().setPosX(xSiguiente);
        caja.getCoordenada().setPosY(ySiguiente);
        grillaSuperior.get(ySiguiente).set(xSiguiente, caja);
        grillaSuperior.get(yActual).set(xActual, null);

        int pushes = 1;

        if (!caja.alSerEmpujada(onGameOver)) {
            return pushes;
        }

        ElementoBase terreno = grillaInferior.get(ySiguiente).get(xSiguiente);
        if (terreno.esResbaloso()) {
            pushes += deslizarCaja(caja, dx, dy);
        }

        return pushes;
    }

    public void suscribirVista(TableroPanel tableroPanel) {
        this.tableroPanel = tableroPanel;
    }

    public void desuscribirVista() {
        this.tableroPanel = null;
    }

    private void notificarVista() {
        if (tableroPanel != null) {
            tableroPanel.actualizar(this);
        }
    }

    private boolean hayCajaLlaveSobreCerrojo() {
        boolean encontrado = false;
        for (Caja caja : cajas) {
            if (!encontrado && caja.esCajaLlave()) {
                for (Destino destino : objetivos) {
                    if (!encontrado && destino.esCerrojo()
                            && caja.getCoordenada().equals(destino.getCoordenada())) {
                        encontrado = true;
                    }
                }
            }
        }
        return encontrado;
    }

    private void actualizarRejas() {
        boolean cerrar = !hayCajaLlaveSobreCerrojo();

        if (cerrar) {
            for (List<ElementoBase> fila : grillaInferior) {
                for (ElementoBase elem : fila) {
                    if (elem.getEstadoReja() == Estado.ABIERTO) {
                        int rx = elem.getCoordenada().getPosX();
                        int ry = elem.getCoordenada().getPosY();
                        if (grillaSuperior.get(ry).get(rx) != null) {
                            cerrar = false;
                        }
                    }
                }
            }
        }

        Estado nuevoEstado = cerrar ? Estado.CERRADO : Estado.ABIERTO;
        for (List<ElementoBase> fila : grillaInferior) {
            for (ElementoBase elem : fila) {
                if (elem.getEstadoReja() != null) {
                    elem.setEstadoReja(nuevoEstado);
                }
            }
        }
    }

    public int getCajasEnDestino() {
        int count = 0;
        for (var caja : cajas) {
            boolean contada = false;
            for (var destino : objetivos) {
                if (!contada && caja.getCoordenada().equals(destino.getCoordenada())) {
                    if (!destino.esCerrojo() || caja.esCajaLlave()) {
                        contada = true;
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // TODO: modificar el método para que funcione con la implementación de dos grillas
    /*@Override
    public String toString() {
        StringBuilder tableroTexto = new StringBuilder();

        for (List<ElementoBase> fila : grilla) {
            for (ElementoBase elemento : fila) {
                tableroTexto.append(elemento.getSimbolo()).append(' ');
            }
            tableroTexto.append(System.lineSeparator());
        }

        return tableroTexto.toString();
    }*/
}
