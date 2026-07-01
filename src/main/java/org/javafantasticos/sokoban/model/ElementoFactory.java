package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.cajas.CajaFragil;
import org.javafantasticos.sokoban.model.cajas.CajaLlave;
import org.javafantasticos.sokoban.model.cajas.CajaNormal;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.items.Bomba;
import org.javafantasticos.sokoban.model.items.ItemMoneda;
import org.javafantasticos.sokoban.model.items.ItemUndoExtra;
import org.javafantasticos.sokoban.model.muros.Pared;
import org.javafantasticos.sokoban.model.muros.Reja;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.suelo.Aceite;
import org.javafantasticos.sokoban.model.suelo.Cerrojo;
import org.javafantasticos.sokoban.model.suelo.Destino;
import org.javafantasticos.sokoban.model.suelo.Suelo;
/**
 * Fábrica que se dedica a instanciar las entidades individuales del juego.
 * Traduce un caracter específico ('P', 'S', 'N', 'F', etc.) en su objeto correspondiente
 * (Pared, Suelo, CajaNormal, CajaFragil, etc.) asignándole sus coordenadas por ahora...
 * Centraliza la creación de objetos.
 *
 * Patrón Singleton: una única fábrica de elementos para todo el juego.
 */
public class ElementoFactory {
    private static ElementoFactory instancia;

    private ElementoFactory() {}

    public static ElementoFactory getInstancia() {
        if (instancia == null) {
            instancia = new ElementoFactory();
        }
        return instancia;
    }

    public ElementoBase crearElementoEstatico(char simbolo, Coordenada coordenada) {
        return switch (simbolo) {
            case 'P' -> new Pared(coordenada);
            case 'S' -> new Suelo(coordenada);
            case 'C' -> new Cerrojo(coordenada);
            case 'R' -> new Reja(coordenada);
            case 'A' -> new Aceite(coordenada);
            case 'M' -> new ItemMoneda(coordenada);
            case 'B' -> new Bomba(coordenada);
            case 'U' -> new ItemUndoExtra(coordenada);
            default -> throw new IllegalArgumentException("Simbolo de tablero desconocido: " + simbolo);
        };
    }

    public Caja crearCaja(char simbolo, Coordenada coordenada) {
        return switch (simbolo) {
            case 'N' -> new CajaNormal(coordenada);
            case 'F' -> new CajaFragil(coordenada);
            case 'K' -> new CajaLlave(coordenada);
            default -> throw new IllegalArgumentException("Simbolo de caja desconocido: " + simbolo);
        };
    }

    public Destino crearDestino(Coordenada coordenada) {
        return new Destino(coordenada);
    }

    public Jugador crearJugador(Coordenada coordenada) {
        return new Jugador(coordenada);
    }

    public Suelo crearSuelo(Coordenada coordenada) {
        return new Suelo(coordenada);
    }
}
