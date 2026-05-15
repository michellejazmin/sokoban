package org.example;

import org.example.interfaces.ElementoTablero;
import org.example.model.LevelsExtractor;
import org.example.model.Tablero;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LevelsExtractor levelsExtractor = new LevelsExtractor();
        Tablero primerTablero = levelsExtractor.getTablero(0);

        System.out.println(primerTablero.getNombre());

        for (List<ElementoTablero> fila : primerTablero.getGrilla()) {
            for (ElementoTablero elemento : fila) {
                System.out.print(elemento.getSimbolo() + " ");
            }
            System.out.println();
        }
    }
}
