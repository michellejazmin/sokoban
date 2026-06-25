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
 *
 * Patrón Singleton: existe una única instancia durante toda la partida que
 * conserva el progreso del jugador (lista de tableros cargados y nivel actual).
 * Garantizar una sola instancia evita inconsistencias entre el nivel que ve la
 * vista y el que tiene el controller, y permite cargar los tableros del .txt
 * una única vez.
 *
 * No se usa sincronización porque toda la aplicación Swing corre en el EDT
 * (Event Dispatch Thread), por lo que no hay acceso concurrente.
 */
public final class GestorNiveles {
    private static GestorNiveles instancia;

    private final List<Tablero> tableros;
    private int nivelActualIndex;

    private GestorNiveles(LectorNiveles lector, TableroFactory factory) {
        this.tableros = inicializarTableros(lector, factory);
        this.nivelActualIndex = 0;
    }

    /**
     * Inicializa el Singleton la primera vez y devuelve la instancia.
     * Las llamadas posteriores con parámetros ignoran los argumentos y
     * devuelven la instancia ya creada.
     */
    public static GestorNiveles getInstancia(LectorNiveles lector, TableroFactory factory) {
        if (instancia == null) {
            instancia = new GestorNiveles(lector, factory);
        }
        return instancia;
    }

    /**
     * Devuelve la instancia ya creada. Lanza excepción si nunca se inicializó,
     * para fallar rápido en lugar de devolver null.
     */
    public static GestorNiveles getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException(
                    "GestorNiveles no fue inicializado. Llamar primero a getInstancia(lector, factory).");
        }
        return instancia;
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