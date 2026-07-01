package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.muros.Reja;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Cerrojo;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.model.suelo.Suelo;

import java.util.ArrayList;
import java.util.List;
/**
 * Actúa como ensamblador del nivel.
 * Recibe la matriz de texto cruda (dada por LevelsExtractor), la recorre y
 * delega la creación de cada objeto individual al ElementoFactory.
 * Su responsabilidad es organizar estas entidades recién creadas en las
 * estructuras de datos correctas (grilla, lista de cajas, lista de objetivos)
 * para construir y retornar un objeto Tablero listo para ser jugado.
 * Patrón Singleton: una única fábrica de tableros para todo el juego.
 */
public final class TableroFactory {
    private static TableroFactory instancia;

    private final ElementoFactory elementoFactory;

    private TableroFactory() {
        this.elementoFactory = ElementoFactory.getInstancia();
    }

    public static TableroFactory getInstancia() {
        if (instancia == null) {
            instancia = new TableroFactory();
        }
        return instancia;
    }

    public Tablero crearTablero(String nombre, List<String> filasTexto) {
        List<List<ElementoBase>> grillaInferior = new ArrayList<>();
        List<List<ElementoBase>> grillaSuperior = new ArrayList<>();
        List<Caja> cajas = new ArrayList<>();
        List<Destino> objetivos = new ArrayList<>();
        List<Reja> rejas = new ArrayList<>();
        Jugador jugador = null;

        for (int posY = 0; posY < filasTexto.size(); posY++) {
            String[] simbolos = filasTexto.get(posY).split(",");
            List<ElementoBase> filaInferior = new ArrayList<>();
            List<ElementoBase> filaSuperior = new ArrayList<>();

            for (int posX = 0; posX < simbolos.length; posX++) {
                char simbolo = simbolos[posX].trim().charAt(0);
                Coordenada coordenada = new Coordenada(posX, posY);

                switch (simbolo) {
                    case 'N', 'F', 'K' -> {
                        Caja caja = elementoFactory.crearCaja(simbolo, coordenada);
                        Suelo suelo = elementoFactory.crearSuelo(coordenada);
                        cajas.add(caja);
                        filaSuperior.add(caja);
                        filaInferior.add(suelo);
                    }
                    case 'D' -> {
                        Destino destino = elementoFactory.crearDestino(coordenada);
                        objetivos.add(destino);
                        filaInferior.add(destino);
                        filaSuperior.add(null);
                    }
                    case 'J' -> {
                        jugador = elementoFactory.crearJugador(coordenada);
                        Suelo suelo = elementoFactory.crearSuelo(coordenada);
                        filaSuperior.add(jugador);
                        filaInferior.add(suelo);
                    }
                    case 'C' -> {
                        Cerrojo cerrojo = new Cerrojo(coordenada);
                        objetivos.add(cerrojo);
                        filaInferior.add(cerrojo);
                        filaSuperior.add(null);
                    }
                    case 'R' -> {
                        Reja reja = new Reja(coordenada);
                        rejas.add(reja);
                        filaInferior.add(reja);
                        filaSuperior.add(null);
                    }
                    default -> {
                        filaInferior.add(elementoFactory.crearElementoEstatico(simbolo, coordenada));
                        filaSuperior.add(null);
                    }
                }
            }

            grillaInferior.add(filaInferior);
            grillaSuperior.add(filaSuperior);
        }

        return new Tablero(nombre, grillaInferior, grillaSuperior, cajas, objetivos, rejas, jugador);
    }
}