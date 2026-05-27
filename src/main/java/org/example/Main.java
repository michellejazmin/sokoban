package org.example;

import org.example.interfaces.LectorNiveles;
import org.example.model.GestorNiveles;
import org.example.model.TableroFactory;
import org.example.model.TxtLevelsExtractor;
import org.example.model.Tablero;
<<<<<<< HEAD
import org.example.model.player.Jugador;
=======
import org.example.controller.GameController;
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
import org.example.model.player.MovimientoTeclado;

public class Main {
    public static void main(String[] args) {
        LectorNiveles lectorTxt = new TxtLevelsExtractor("/TableroXNivel");

        TableroFactory factory = new TableroFactory();

<<<<<<< HEAD
        GestorNiveles gestor = new GestorNiveles(lectorTxt, factory);

        Tablero tableroParaJugar = gestor.getTableroActual();
        Jugador jugador = tableroParaJugar.getJugador();
        MovimientoTeclado movimientoTeclado = new MovimientoTeclado(jugador); // se encapsula despues en el constructo de la view
=======
        GestorNiveles gestor = GestorNiveles.getInstancia(lectorTxt, factory);

        Tablero tableroParaJugar = gestor.getTableroActual();
        GameController controller = new GameController(tableroParaJugar);
        MovimientoTeclado movimientoTeclado = new MovimientoTeclado(controller); // se encapsula despues en el constructor de la view
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)

        System.out.println(tableroParaJugar);
    }
}
