package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.view.TableroPanel;

import java.util.ArrayList;
import java.util.List;

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

    public void mover(int dx, int dy) {
        int xJugador = jugador.getCoordenada().getPosX();
        int yJugador = jugador.getCoordenada().getPosY();
        int xDestinoJugador = xJugador + dx;
        int yDestinoJugador = yJugador + dy;

        ElementoBase elementoAdyacente = getElemento(xDestinoJugador, yDestinoJugador);

        if (elementoAdyacente.bloqueaPaso()) return;

        // Si el elemento adyacente es una caja, intentamos moverla
        if (elementoAdyacente.esMovible()) {
            int xDestinoCaja = xDestinoJugador + dx;
            int yDestinoCaja = yDestinoJugador + dy;
            ElementoBase destinoCaja = getElemento(xDestinoCaja, yDestinoCaja);

            // Si lo que hay detrás de la caja bloquea, no podemos mover
            if (!destinoCaja.esOcupable()) return;

            elementoAdyacente.getCoordenada().setPosX(xDestinoCaja);
            elementoAdyacente.getCoordenada().setPosY(yDestinoCaja);
            grillaSuperior.get(yDestinoCaja).set(xDestinoCaja, elementoAdyacente);
        }

        jugador.getCoordenada().setPosX(xDestinoJugador);
        jugador.getCoordenada().setPosY(yDestinoJugador);
        grillaSuperior.get(yJugador).set(xJugador, null);
        grillaSuperior.get(yDestinoJugador).set(xDestinoJugador, jugador);

        if (elementoAdyacente.esResbaloso()) {
            mover(dx, dy);
        }

        notificarVista();

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

    private List<List<ElementoBase>> copiarGrilla(List<List<ElementoBase>> grilla) {
        List<List<ElementoBase>> copia = new ArrayList<>();

        for (List<ElementoBase> fila : grilla) {
            copia.add(List.copyOf(fila));
        }

        return List.copyOf(copia);
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
