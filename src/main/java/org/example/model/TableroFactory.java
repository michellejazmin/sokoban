package org.example.model;

import org.example.interfaces.ElementoTablero;
import org.example.model.cajas.Caja;
import org.example.model.dto.Coordenada;
import org.example.model.suelo.Destino;

import java.util.ArrayList;
import java.util.List;

public class TableroFactory {

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