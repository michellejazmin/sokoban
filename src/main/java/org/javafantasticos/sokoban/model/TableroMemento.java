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
    private final List<int[]> cajasCoords;
    private final List<Integer> cajasTtl;
    private final List<List<ElementoBase>> grillaInferior;
    private final List<List<ElementoBase>> grillaSuperior;
    private final Map<ElementoBase, EstadoReja> estadosReja;
    private final ElementoBase elementoBajoJugador;

    TableroMemento(Tablero tablero) {
        Jugador jugador = tablero.getJugador();
        this.jugadorX = jugador.getCoordenada().getPosX();
        this.jugadorY = jugador.getCoordenada().getPosY();
        this.orientacion = jugador.getOrientacion();

        List<Caja> cajas = tablero.getCajas();
        this.cajasCoords = new ArrayList<>(cajas.size());
        this.cajasTtl = new ArrayList<>(cajas.size());
        for (Caja caja : cajas) {
            this.cajasCoords.add(new int[]{caja.getCoordenada().getPosX(), caja.getCoordenada().getPosY()});
            this.cajasTtl.add(caja.getTtl());
        }

        this.grillaInferior = copiarGrilla(tablero.getGrillaInferior());
        this.grillaSuperior = copiarGrilla(tablero.getGrillaSuperior());
        this.estadosReja = new IdentityHashMap<>();
        recolectarEstadosReja(tablero.getGrillaInferior());
        recolectarEstadosReja(tablero.getGrillaSuperior());

        this.elementoBajoJugador = tablero.getElementoBajoJugador();
    }

    private List<List<ElementoBase>> copiarGrilla(List<List<ElementoBase>> grilla) {
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

        for (int i = 0; i < cajasCoords.size(); i++) {
            int[] coords = cajasCoords.get(i);
            Integer ttl = cajasTtl.get(i);

            if (ttl != null && coords[1] >= 0 && coords[1] < grillaSuperior.size()
                && coords[0] >= 0 && coords[0] < grillaSuperior.get(coords[1]).size()) {
                ElementoBase elem = grillaSuperior.get(coords[1]).get(coords[0]);

                if (elem != null) {
                    elem.setTtl(ttl);
                }
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
