package org.javafantasticos.sokoban;

import org.javafantasticos.sokoban.controller.GameController;
import org.javafantasticos.sokoban.interfaces.LectorNiveles;
import org.javafantasticos.sokoban.model.GestorNiveles;
import org.javafantasticos.sokoban.model.TableroFactory;
import org.javafantasticos.sokoban.model.TxtLevelsExtractor;
import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.view.VistaJuego;

public class Main {
    public static void main(String[] args) {
        LectorNiveles lectorTxt = new TxtLevelsExtractor("/TableroXNivel");

        TableroFactory factory = new TableroFactory();

        GestorNiveles gestor = GestorNiveles.getInstancia(lectorTxt, factory);

        Tablero tableroParaJugar = gestor.getTableroActual();
        GameController controller = new GameController(tableroParaJugar);

        new VistaJuego(tableroParaJugar, controller);
    }
}
