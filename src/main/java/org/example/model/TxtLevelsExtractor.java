package org.example.model;

import org.example.interfaces.LectorNiveles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación concreta que extrae niveles leyendo un archivo de texto (.txt).
 */
public final class TxtLevelsExtractor implements LectorNiveles {
    private final String rutaArchivo;

    public TxtLevelsExtractor(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    @Override
    public List<List<String>> extraerNivelesTexto() {
        InputStream inputStream = getClass().getResourceAsStream(rutaArchivo);

        if (inputStream == null) {
            throw new IllegalArgumentException("No se encontro el archivo de niveles: " + rutaArchivo);
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