package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.LectorNiveles;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.TableroFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra los niveles del juego.
 * Orquesta la lectura de datos crudos (vía LectorNiveles) y la
 * instanciación de los mapas (vía TableroFactory).
 * Singleton: existe una única instancia durante toda la partida.
 */
// TODO: Implementar Singleton?
public final class GestorNiveles {
    private final List<Tablero> tableros;
    private int nivelActualIndex;

    public GestorNiveles(LectorNiveles lector, TableroFactory factory) {
        this.tableros = inicializarTableros(lector, factory);
        this.nivelActualIndex = 0;
    }

    private List<Tablero> inicializarTableros(LectorNiveles lector, TableroFactory factory) {
        List<List<String>> tablerosTexto = lector.extraerNivelesTexto();
        List<Tablero> tablerosArmados = new ArrayList<>();

        for (int i = 0; i < tablerosTexto.size(); i++) {
            tablerosArmados.add(factory.crearTablero("Nivel " + (i + 1), tablerosTexto.get(i)));
        }

        return List.copyOf(tablerosArmados);
    }

    public Tablero getTableroActual() {
        return tableros.get(nivelActualIndex);
    }

    public List<Tablero> getTodosLosTableros() {
        return tableros; // Ya es inmodificable por el List.copyOf()
    }

    public boolean avanzarNivel() {
        if (nivelActualIndex < tableros.size() - 1) {
            nivelActualIndex++;
            return true;
        }
        return false; // Ya no hay más niveles
    }
}