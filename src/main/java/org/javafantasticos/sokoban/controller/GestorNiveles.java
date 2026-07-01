package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.model.TableroFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra los niveles del juego.
 * Orquesta la lectura de datos crudos (vía LectorNiveles) y la
 * instanciación de los mapas (vía TableroFactory).
 * Patrón Singleton: existe una única instancia durante toda la partida que
 * conserva el progreso del jugador (lista de tableros cargados y nivel actual).
 * Garantizar una sola instancia evita inconsistencias entre el nivel que ve la
 * vista y el que tiene el controller, y permite cargar los tableros del .txt
 * una única vez.
 * No se usa sincronización porque toda la aplicación Swing corre en el EDT
 * (Event Dispatch Thread), por lo que no hay acceso concurrente.
 */
public final class GestorNiveles {
    private static GestorNiveles instancia;

    private final TableroFactory factory;
    private final List<List<String>> nivelesTexto;
    private List<Tablero> tableros;
    private int nivelActualIndex;

    private GestorNiveles() {
        this.factory = TableroFactory.getInstancia();
        this.nivelesTexto = TxtLevelsExtractor.getInstancia().extraerNivelesTexto();
        this.tableros = inicializarTableros();
        this.nivelActualIndex = 0;
    }

    public static GestorNiveles getInstancia() {
        if (instancia == null) {
            instancia = new GestorNiveles();
        }
        return instancia;
    }

    private List<Tablero> inicializarTableros() {
        List<Tablero> tablerosArmados = new ArrayList<>();

        for (int i = 0; i < nivelesTexto.size(); i++) {
            tablerosArmados.add(factory.crearTablero("Nivel " + (i + 1), nivelesTexto.get(i)));
        }

        return tablerosArmados;
    }

    public Tablero getTableroActual() {
        return tableros.get(nivelActualIndex);
    }

    public int getNivelActualIndex() {
        return nivelActualIndex;
    }

    public int getTotalNiveles() {
        return tableros.size();
    }

    /**
     * Reemplaza el tablero del nivel actual por uno recién creado desde el texto original.
     */
    public Tablero reiniciarNivelActual() {
        Tablero nuevo = factory.crearTablero("Nivel " + (nivelActualIndex + 1), nivelesTexto.get(nivelActualIndex));
        tableros.set(nivelActualIndex, nuevo);
        return nuevo;
    }

    public boolean avanzarNivel() {
        if (nivelActualIndex < tableros.size() - 1) {
            nivelActualIndex++;
            return true;
        }
        return false; // Ya no hay más niveles
    }

    /**
     * Reinicia el progreso del juego al nivel 1 con un tablero fresco.
     */
    public void reiniciarProgreso() {
        this.tableros = inicializarTableros();
        nivelActualIndex = 0;
        reiniciarNivelActual();
    }
}