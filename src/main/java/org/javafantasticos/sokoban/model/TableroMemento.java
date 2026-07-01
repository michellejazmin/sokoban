package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.model.muros.EstadoReja;
import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.player.Jugador;
import org.javafantasticos.sokoban.model.player.Orientacion;

import java.util.*;

public class TableroMemento {
    private final int jugadorX;
    private final int jugadorY;
    private final Orientacion orientacion;
    private final Map<Caja, int[]> cajasCoords;
    private final Map<Caja, Integer> cajasTtl;
    private final List<List<ElementoBase>> grillaInferior;
    private final List<List<ElementoBase>> grillaSuperior;
    private final Map<ElementoBase, EstadoReja> estadosReja;
    private final ElementoBase elementoBajoJugador;

    TableroMemento(Tablero tablero) {
        Jugador jugador = tablero.getJugador();
        this.jugadorX = jugador.getCoordenada().getPosX();
        this.jugadorY = jugador.getCoordenada().getPosY();
        this.orientacion = jugador.getOrientacion();

        this.cajasCoords = new IdentityHashMap<>();
        this.cajasTtl = new IdentityHashMap<>();
        for (Caja caja : tablero.getCajas()) {
            cajasCoords.put(caja, new int[]{caja.getCoordenada().getPosX(), caja.getCoordenada().getPosY()});
            cajasTtl.put(caja, caja.getTtl());
        }

        this.grillaInferior = copiarConEstados(tablero.getGrillaInferior());
        this.grillaSuperior = copiarConEstados(tablero.getGrillaSuperior());
        this.estadosReja = new IdentityHashMap<>();
        recolectarEstadosReja(tablero.getGrillaInferior());
        recolectarEstadosReja(tablero.getGrillaSuperior());

        this.elementoBajoJugador = tablero.getElementoBajoJugador();
    }

    private List<List<ElementoBase>> copiarConEstados(List<List<ElementoBase>> grilla) {
        List<List<ElementoBase>> copia = new ArrayList<>();
        for (List<ElementoBase> fila : grilla) {
            copia.add(new ArrayList<>(fila));
        }
        return copia;
    }

    private void recolectarEstadosReja(List<List<ElementoBase>> grilla) {
        for (List<ElementoBase> fila : grilla) {
            for (ElementoBase elem : fila) {
                if (elem == null) continue;
                EstadoReja estadoReja = elem.getEstadoReja();
                if (estadoReja != null) {
                    estadosReja.put(elem, estadoReja);
                }
            }
        }
    }

    void restaurar(Tablero tablero) {
        tablero.getJugador().getCoordenada().setPosX(jugadorX);
        tablero.getJugador().getCoordenada().setPosY(jugadorY);
        tablero.getJugador().setOrientacion(orientacion);

        for (Caja caja : tablero.getCajas()) {
            int[] coords = cajasCoords.get(caja);
            if (coords != null) {
                caja.getCoordenada().setPosX(coords[0]);
                caja.getCoordenada().setPosY(coords[1]);
            }
            Integer ttl = cajasTtl.get(caja);
            if (ttl != null) {
                caja.setTtl(ttl);
            }
        }

        restaurarGrilla(tablero.getGrillaInferior(), grillaInferior);
        restaurarGrilla(tablero.getGrillaSuperior(), grillaSuperior);

        for (Map.Entry<ElementoBase, EstadoReja> entry : estadosReja.entrySet()) {
            entry.getKey().setEstadoReja(entry.getValue());
        }

        tablero.setElementoBajoJugador(this.elementoBajoJugador);
    }

    private void restaurarGrilla(List<List<ElementoBase>> destino, List<List<ElementoBase>> origen) {
        destino.clear();
        for (List<ElementoBase> fila : origen) {
            destino.add(new ArrayList<>(fila));
        }
    }
}
