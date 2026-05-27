package org.example;

import org.example.interfaces.LectorNiveles;
import org.example.model.GestorNiveles;
import org.example.model.TableroFactory;
import org.example.model.TxtLevelsExtractor;
import org.example.model.Tablero;
import org.example.controller.GameController;
import org.example.model.player.MovimientoTeclado;

public class Main {
    public static void main(String[] args) {
        LectorNiveles lectorTxt = new TxtLevelsExtractor("/TableroXNivel");

        TableroFactory factory = new TableroFactory();

        GestorNiveles gestor = GestorNiveles.getInstancia(lectorTxt, factory);

        Tablero tableroParaJugar = gestor.getTableroActual();
        GameController controller = new GameController(tableroParaJugar);
        MovimientoTeclado movimientoTeclado = new MovimientoTeclado(controller); // se encapsula despues en el constructor de la view

        System.out.println(tableroParaJugar);
    }
}
