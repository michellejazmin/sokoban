package org.example.model;

import org.example.interfaces.ElementoTablero;
import org.example.model.cajas.Caja;
import org.example.model.cajas.CajaFragil;
import org.example.model.cajas.CajaLlave;
import org.example.model.cajas.CajaNormal;
import org.example.model.pared.Pared;
import org.example.model.pared.RejaAbierta;
import org.example.model.pared.RejaCerrada;
import org.example.model.suelo.Aceite;
import org.example.model.suelo.Cerrojo;
import org.example.model.suelo.Destino;
import org.example.model.suelo.Suelo;

import java.util.ArrayList;
import java.util.List;

public class TableroFactory {
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
                        Caja caja = crearCaja(simbolo, coordenada);
                        cajas.add(caja);
                        fila.add(caja);
                    }
                    case 'D' -> {
                        Destino destino = crearDestino(coordenada);
                        objetivos.add(destino);
                        fila.add(destino);
                    }
                    case 'J' -> {
                        jugador = crearJugador(coordenada);
                        fila.add(jugador);
                    }
                    default -> fila.add(crearElemento(simbolo, coordenada));
                }
            }

            grilla.add(fila);
        }

        return new Tablero(nombre, grilla, cajas, objetivos, jugador);
    }

    protected ElementoTablero crearElemento(char simbolo, Coordenada coordenada) {
        return switch (simbolo) {
            case 'P' -> new Pared(coordenada);
            case 'S' -> new Suelo(coordenada);
            case 'C' -> new Cerrojo(coordenada);
            case 'L' -> new RejaCerrada(coordenada);
            case 'O' -> new RejaAbierta(coordenada);
            case 'A' -> new Aceite(coordenada);
            default -> throw new IllegalArgumentException("Simbolo de tablero desconocido: " + simbolo);
        };
    }

    protected Caja crearCaja(char simbolo, Coordenada coordenada) {
        return switch (simbolo) {
            case 'N' -> new CajaNormal(coordenada);
            case 'F' -> new CajaFragil(coordenada);
            case 'K' -> new CajaLlave(coordenada);
            default -> throw new IllegalArgumentException("Simbolo de caja desconocido: " + simbolo);
        };
    }

    protected Destino crearDestino(Coordenada coordenada) {
        return new Destino(coordenada);
    }

    protected Jugador crearJugador(Coordenada coordenada) {
        return new Jugador(coordenada);
    }
}
