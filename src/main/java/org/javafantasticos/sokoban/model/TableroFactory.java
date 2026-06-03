package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.ElementoTablero;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Destino;

import java.util.ArrayList;
import java.util.List;
/**
 * Actúa como ensamblador del nivel.
 * Recibe la matriz de texto cruda (dada por LevelsExtractor), la recorre y
 * delega la creación de cada objeto individual al ElementoFactory.
 * Su responsabilidad es organizar estas entidades recién creadas en las
 * estructuras de datos correctas (grilla, lista de cajas, lista de objetivos)
 * para construir y retornar un objeto Tablero listo para ser jugado.
 */
public final class TableroFactory {

    private final ElementoFactory elementoFactory;

    public TableroFactory() {
        this.elementoFactory = new ElementoFactory();
    }

    public Tablero crearTablero(String nombre, List<String> filasTexto) {
        List<List<ElementoTablero>> grilla = new ArrayList<>();
        List<Caja> cajas = new ArrayList<>();
        List<Destino> objetivos = new ArrayList<>();
        Jugador jugador = null;

        for (int posY = 0; posY < filasTexto.size(); posY++) {
            String[] simbolos = filasTexto.get(posY).split(",");
            List<ElementoTablero> fila = new ArrayList<>();

            for (int posX = 0; posX < simbolos.length; posX++) {
                char simbolo = simbolos[posX].trim().charAt(0);
                Coordenada coordenada = new Coordenada(posX, posY);

                switch (simbolo) {
                    case 'N', 'F', 'K' -> {
                        Caja caja = elementoFactory.crearCaja(simbolo, coordenada);
                        cajas.add(caja);
                        fila.add(caja);
                    }
                    case 'D' -> {
                        Destino destino = elementoFactory.crearDestino(coordenada);
                        objetivos.add(destino);
                        fila.add(destino);
                    }
                    case 'J' -> {
                        jugador = elementoFactory.crearJugador(coordenada);
                        fila.add(jugador);
                    }
                    default -> fila.add(elementoFactory.crearElementoEstatico(simbolo, coordenada));
                }
            }

            grilla.add(fila);
        }

        return new Tablero(nombre, grilla, cajas, objetivos, jugador);
    }
}