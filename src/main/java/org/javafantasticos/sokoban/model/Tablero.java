package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.view.TableroPanel;

import java.util.List;
import java.util.function.BiConsumer;

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
    private BiConsumer<TableroMemento, Boolean> onStateChange;
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

    public void setOnStateChange(BiConsumer<TableroMemento, Boolean> callback) {
        this.onStateChange = callback;
    }

    public TableroMemento crearMemento() {
        return new TableroMemento(this);
    }

    public void restaurar(TableroMemento memento) {
        memento.restaurar(this);
    }

    /**
     * @return cantidad de cajas empujadas en este paso (incluyendo recursivos por aceite)
     */
    public int mover(int dx, int dy) {
        int xJugador = jugador.getCoordenada().getPosX();
        int yJugador = jugador.getCoordenada().getPosY();
        int xDestinoJugador = xJugador + dx;
        int yDestinoJugador = yJugador + dy;

        ElementoBase elementoAdyacente = getElemento(xDestinoJugador, yDestinoJugador);

        if (elementoAdyacente.bloqueaPaso()) return 0;

        boolean pushEnEstePaso = false;

        // Si el elemento adyacente es una caja, intentamos moverla
        if (elementoAdyacente.esMovible()) {
            int xDestinoCaja = xDestinoJugador + dx;
            int yDestinoCaja = yDestinoJugador + dy;
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

        if (onStateChange != null) {
            onStateChange.accept(new TableroMemento(this), pushEnEstePaso);
        }

        int pushes = pushEnEstePaso ? 1 : 0;
        if (elementoAdyacente.esResbaloso()) {
            pushes += mover(dx, dy);
        }

        notificarVista();
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

    public int getCajasEnDestino() {
        int count = 0;
        for (var caja : cajas) {
            for (var destino : objetivos) {
                if (caja.getCoordenada().equals(destino.getCoordenada())) {
                    count++;
                    break;
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
