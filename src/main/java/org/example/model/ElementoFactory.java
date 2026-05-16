package org.example.model;

import org.example.interfaces.ElementoTablero;
import org.example.model.cajas.Caja;
import org.example.model.cajas.CajaFragil;
import org.example.model.cajas.CajaLlave;
import org.example.model.cajas.CajaNormal;
import org.example.model.dto.Coordenada;
import org.example.model.muros.Pared;
import org.example.model.muros.Reja;
import org.example.model.suelo.Aceite;
import org.example.model.suelo.Cerrojo;
import org.example.model.suelo.Destino;
import org.example.model.suelo.Suelo;

public class ElementoFactory {

    public ElementoTablero crearElementoEstatico(char simbolo, Coordenada coordenada) {
        return switch (simbolo) {
            case 'P' -> new Pared(coordenada);
            case 'S' -> new Suelo(coordenada);
            case 'C' -> new Cerrojo(coordenada);
            case 'R' -> new Reja(coordenada);
            case 'A' -> new Aceite(coordenada);
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
}
