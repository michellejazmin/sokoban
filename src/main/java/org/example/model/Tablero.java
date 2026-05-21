package org.example.model;

import org.example.interfaces.ElementoTablero;
import org.example.model.cajas.Caja;
import org.example.model.player.Jugador;
import org.example.model.suelo.Destino;

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

    public Tablero(String nombre, List<List<ElementoTablero>> grilla, List<Caja> cajas, List<Destino> objetivos, Jugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El tablero no tiene jugador");
        }

        this.nombre = nombre;
        this.grilla = grilla;
        this.cajas = cajas;
        this.objetivos = List.copyOf(objetivos);
        this.jugador = jugador;
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
