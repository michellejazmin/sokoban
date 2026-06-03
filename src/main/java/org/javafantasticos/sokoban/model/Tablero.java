package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.ElementoTablero;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.model.suelo.Suelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa el estado actual de un nivel del juego.
 * Mantiene la disposición de la grilla, las cajas, los objetivos y el jugador.
 */

public class Tablero {
    private final String nombre;
    private final List<List<ElementoTablero>> grilla;
    private final List<Caja> cajas;
    private final List<Destino> objetivos;
    private final Jugador jugador;
    private ElementoTablero elementoBajoJugador; // lo que había en la celda antes de que el jugador la pisara

    public Tablero(String nombre, List<List<ElementoTablero>> grilla, List<Caja> cajas, List<Destino> objetivos, Jugador jugador) {
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

    public List<List<ElementoTablero>> getGrilla() {
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

    public ElementoTablero getElemento(int posX, int posY) {
        return grilla.get(posY).get(posX);
    }

    public boolean mover(int dx, int dy) {
        int oldX = jugador.getCoordenada().getPosX();
        int oldY = jugador.getCoordenada().getPosY();
        int x = oldX + dx;
        int y = oldY + dy;

        ElementoTablero siguiente = getElemento(x, y);

        if (siguiente.bloqueaPaso()) return false;

        // TODO: empujar caja si hay una adelante

        // Restaurar la celda anterior con lo que había debajo
        grilla.get(oldY).set(oldX, elementoBajoJugador);

        // Guardar lo que hay debajo del nuevo destino
        elementoBajoJugador = siguiente;

        // Mover jugador en coordenada y en grilla
        jugador.getCoordenada().setPosX(x);
        jugador.getCoordenada().setPosY(y);
        grilla.get(y).set(x, jugador);

        if (siguiente.esResbaloso()) {
            mover(dx, dy);
        }

        return true;
    }

    private List<List<ElementoTablero>> copiarGrilla(List<List<ElementoTablero>> grilla) {
        List<List<ElementoTablero>> copia = new ArrayList<>();

        for (List<ElementoTablero> fila : grilla) {
            copia.add(List.copyOf(fila));
        }

        return List.copyOf(copia);
    }

    @Override
    public String toString() {
        StringBuilder tableroTexto = new StringBuilder();

        for (List<ElementoTablero> fila : grilla) {
            for (ElementoTablero elemento : fila) {
                tableroTexto.append(elemento.getSimbolo()).append(' ');
            }
            tableroTexto.append(System.lineSeparator());
        }

        return tableroTexto.toString();
    }
}
