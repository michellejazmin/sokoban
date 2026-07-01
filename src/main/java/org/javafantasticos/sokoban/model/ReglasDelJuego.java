package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.model.cajas.Caja;
import org.javafantasticos.sokoban.model.suelo.Destino;

import java.util.List;

public class ReglasDelJuego {

    public static boolean hayCajaLlaveSobreCerrojo(List<Caja> cajas, List<Destino> objetivos) {
        for (Caja caja : cajas) {
            if (caja.esCajaLlave()) {
                for (Destino destino : objetivos) {
                    if (destino.esCerrojo()
                            && caja.getCoordenada().equals(destino.getCoordenada())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int contarCajasEnDestino(List<Caja> cajas, List<Destino> objetivos) {
        int count = 0;
        for (Caja caja : cajas) {
            boolean contada = false;
            for (Destino destino : objetivos) {
                if (!contada && caja.getCoordenada().equals(destino.getCoordenada())) {
                    if (!destino.esCerrojo() || caja.esCajaLlave()) {
                        contada = true;
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
