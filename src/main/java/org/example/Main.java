package org.example;

import org.example.interfaces.ElementoTablero;
import org.example.interfaces.LectorNiveles;
import org.example.model.GestorNiveles;
import org.example.model.TableroFactory;
import org.example.model.TxtLevelsExtractor;
import org.example.model.Tablero;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LectorNiveles lectorTxt = new TxtLevelsExtractor("/TableroXNivel");

        TableroFactory factory = new TableroFactory();

        GestorNiveles gestor = new GestorNiveles(lectorTxt, factory);

        Tablero tableroParaJugar = gestor.getTableroActual();
    }
}
