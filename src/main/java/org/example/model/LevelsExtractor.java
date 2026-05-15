package org.example.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelsExtractor {
    private static final String DEFAULT_LEVELS_FILE = "/TableroXNivel";

    private final List<List<String>> tablerosTexto;
    private final List<Tablero> tableros;

    public LevelsExtractor() {
        this(DEFAULT_LEVELS_FILE);
    }

    public LevelsExtractor(String levelsFile) {
        this.tablerosTexto = cargarTablerosTexto(levelsFile);
        this.tableros = crearTableros(tablerosTexto);
    }

    public List<List<String>> getTablerosTexto() {
        return Collections.unmodifiableList(tablerosTexto);
    }

    public List<String> getTableroTexto(int nivel) {
        return Collections.unmodifiableList(tablerosTexto.get(nivel));
    }

    public List<Tablero> getTableros() {
        return Collections.unmodifiableList(tableros);
    }

    public Tablero getTablero(int nivel) {
        return tableros.get(nivel);
    }

    public int cantidadNiveles() {
        return tableros.size();
    }

    private List<List<String>> cargarTablerosTexto(String levelsFile) {
        InputStream inputStream = LevelsExtractor.class.getResourceAsStream(levelsFile);

        if (inputStream == null) {
            throw new IllegalArgumentException("No se encontro el archivo de niveles: " + levelsFile);
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
            throw new IllegalStateException("No se pudieron cargar los niveles desde: " + levelsFile, e);
        }
    }

    private List<Tablero> crearTableros(List<List<String>> tablerosTexto) {
        TableroFactory tableroFactory = new TableroFactory();
        List<Tablero> resultado = new ArrayList<>();

        for (int i = 0; i < tablerosTexto.size(); i++) {
            resultado.add(tableroFactory.crearTablero("Nivel " + (i + 1), tablerosTexto.get(i)));
        }

        return resultado;
    }

    private void agregarTableroSiTieneFilas(List<List<String>> niveles, List<String> tablero) {
        if (!tablero.isEmpty()) {
            niveles.add(List.copyOf(tablero));
        }
    }
}
