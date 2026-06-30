package org.javafantasticos.sokoban;

import org.javafantasticos.sokoban.controller.GameController;
import org.javafantasticos.sokoban.interfaces.LectorNiveles;
import org.javafantasticos.sokoban.controller.GestorNiveles;
import org.javafantasticos.sokoban.model.TableroFactory;
import org.javafantasticos.sokoban.controller.TxtLevelsExtractor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        LectorNiveles lectorTxt = new TxtLevelsExtractor("/TableroXNivel");
        TableroFactory factory = TableroFactory.getInstancia();

        GestorNiveles gestor = GestorNiveles.getInstancia(lectorTxt, factory);

        SwingUtilities.invokeLater(() -> GameController.getInstancia(gestor));
    }
}
