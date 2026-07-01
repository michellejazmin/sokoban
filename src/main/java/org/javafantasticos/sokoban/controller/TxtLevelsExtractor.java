package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.LectorNiveles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta que extrae niveles leyendo un archivo de texto (.txt).
 * No es Singleton a propósito: es stateless y se inyecta a través de la interfaz
 * {@link LectorNiveles}, lo que permite reemplazarlo (p. ej. por un lector JSON
 * o un mock para tests) sin tocar al resto del sistema.
 */
public final class TxtLevelsExtractor implements LectorNiveles {
    private static TxtLevelsExtractor instancia;
    private String rutaArchivo;

    private TxtLevelsExtractor() {}

    public void setRutaArchivo(String ruta) {
        this.rutaArchivo = ruta;
    }

    public static TxtLevelsExtractor getInstancia() {
        if (instancia == null) {
            instancia = new TxtLevelsExtractor();
        }
        return instancia;
    }

    @Override
    public List<List<String>> extraerNivelesTexto() {
        InputStream inputStream = getClass().getResourceAsStream(rutaArchivo);

        if (inputStream == null) {
            throw new IllegalArgumentException("No se encontró el archivo de niveles: " + rutaArchivo);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<List<String>> niveles = new ArrayList<>();
            List<String> tableroActual = new ArrayList<>();
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank()) {
                    agregarTableroSiTieneFilas(niveles, tableroActual);
                    tableroActual = new ArrayList<>();
                } else {
                    tableroActual.add(linea);
                }
            }

            agregarTableroSiTieneFilas(niveles, tableroActual);
            return niveles;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudieron cargar los niveles desde: " + rutaArchivo, e);
        }
    }

    private void agregarTableroSiTieneFilas(List<List<String>> niveles, List<String> tablero) {
        if (!tablero.isEmpty()) {
            niveles.add(List.copyOf(tablero));
        }
    }
}