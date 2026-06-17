package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.model.suelo.Suelo;
import org.javafantasticos.sokoban.view.TableroPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el estado actual de un nivel del juego.
 * Mantiene la disposición de la grilla, las cajas, los objetivos y el jugador.
 */

public class Tablero {
    private final String nombre;
    private final List<List<ElementoBase>> grilla;
    private final List<Caja> cajas;
    private final List<Destino> objetivos;
    private final Jugador jugador;
    private ElementoBase elementoBajoJugador; // lo que había en la celda antes de que el jugador la pisara
    private ElementoBase elementoBajoCaja; // lo que había en la celda antes de que una caja la pisara
    private Suscriptor tableroPanel;

    public Tablero(String nombre, List<List<ElementoBase>> grilla, List<Caja> cajas, List<Destino> objetivos, Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El tablero no tiene jugador");
        }

        this.nombre = nombre;
        this.grilla = grilla;
        this.cajas = cajas;
        this.objetivos = List.copyOf(objetivos);
        this.jugador = jugador;
        this.elementoBajoJugador = new Suelo(new Coordenada(jugador.getCoordenada().getPosX(), jugador.getCoordenada().getPosY()));
    }

    public String getNombre() {
        return nombre;
    }

    public List<List<ElementoBase>> getGrilla() {
        return grilla;
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
        return grilla.get(posY).get(posX);
    }

    public void mover(int dx, int dy) {
        int xJugador = jugador.getCoordenada().getPosX();
        int yJugador = jugador.getCoordenada().getPosY();
        int xDestinoJugador = xJugador + dx;
        int yDestinoJugador = yJugador + dy;

        ElementoBase siguiente = getElemento(xDestinoJugador, yDestinoJugador);

        if (siguiente.bloqueaPaso()) return;

        // Si el siguiente es una caja, intentamos moverla
        if (siguiente.esMovible()) {
            int xDestinoCaja = xDestinoJugador + dx;
            int yDestinoCaja = yDestinoJugador + dy;
            ElementoBase destinoCaja = getElemento(xDestinoCaja, yDestinoCaja);

            // Si lo que hay detrás de la caja bloquea, no podemos mover
            // TODO: usar esVacio/esOcupable cuando implementemos los dos niveles de tablero
            if (destinoCaja.bloqueaPaso() || destinoCaja.esMovible()) return;

            /*// TODO: Ver si el objeto "siguiente" puede moverse
            // TODO: arreglar este horror
            ElementoBase siguienteDelSiguiente = getElemento(xDestinoJugador + dx, yDestinoJugador + dy);
            if (siguiente.esMovible() && (siguienteDelSiguiente.bloqueaPasoAJugadorMasCaja() || siguienteDelSiguiente.bloqueaPaso())) {
                return;
            }*/

            // TODO: Empujar caja si hay una adelante
            siguiente.actualizar(jugador.getCoordenada(), dx, dy);
            int posXSiguiente = siguiente.getCoordenada().getPosX();
            int posYSiguiente = siguiente.getCoordenada().getPosY();
            grilla.get(posYSiguiente).set(posXSiguiente, siguiente);
            notificarVista();
        }

        // Restaurar la celda anterior con lo que había debajo
        grilla.get(yJugador).set(xJugador, elementoBajoJugador);

        // Guardar lo que hay debajo del nuevo destino
        elementoBajoJugador = siguiente;

        // Mover jugador en coordenada y en grilla
        jugador.getCoordenada().setPosX(xDestinoJugador);
        jugador.getCoordenada().setPosY(yDestinoJugador);
        grilla.get(yDestinoJugador).set(xDestinoJugador, jugador);

        if (siguiente.esResbaloso()) {
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

    @Override
    public String toString() {
        StringBuilder tableroTexto = new StringBuilder();

        for (List<ElementoBase> fila : grilla) {
            for (ElementoBase elemento : fila) {
                tableroTexto.append(elemento.getSimbolo()).append(' ');
            }
            tableroTexto.append(System.lineSeparator());
        }

        return tableroTexto.toString();
    }
}
