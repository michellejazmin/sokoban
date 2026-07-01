package org.javafantasticos.sokoban;

import org.javafantasticos.sokoban.controller.GameController;
import org.javafantasticos.sokoban.interfaces.ILectorNiveles;
import org.javafantasticos.sokoban.controller.TxtLevelsExtractor;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ILectorNiveles lectorTxt = TxtLevelsExtractor.getInstancia();
        lectorTxt.setRutaArchivo("/TableroXNivel");

        SwingUtilities.invokeLater(GameController::getInstancia);
    }
}
