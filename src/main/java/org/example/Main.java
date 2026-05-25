package org.example;

import org.example.interfaces.LectorNiveles;
import org.example.model.GestorNiveles;
import org.example.model.TableroFactory;
import org.example.model.TxtLevelsExtractor;
import org.example.model.Tablero;
import org.example.model.player.Jugador;
import org.example.model.player.MovimientoTeclado;

public class Main {
    public static void main(String[] args) {
        LectorNiveles lectorTxt = new TxtLevelsExtractor("/TableroXNivel");

        TableroFactory factory = new TableroFactory();

        GestorNiveles gestor = new GestorNiveles(lectorTxt, factory);

        Tablero tableroParaJugar = gestor.getTableroActual();
        Jugador jugador = tableroParaJugar.getJugador();
        MovimientoTeclado movimientoTeclado = new MovimientoTeclado(jugador); // se encapsula despues en el constructo de la view

        System.out.println(tableroParaJugar);
    }
}
