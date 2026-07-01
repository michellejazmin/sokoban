package org.javafantasticos.sokoban.interfaces;

import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.TableroMemento;
import org.javafantasticos.sokoban.model.player.Jugador;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface ITableroFisico {
    Jugador getJugador();
    ElementoBase getElemento(int x, int y);
    void limpiarSuperior(int x, int y);
    void asignarSuperior(int x, int y, ElementoBase elem);
    ElementoBase getInferior(int x, int y);
    void asignarInferior(int x, int y, ElementoBase elem);
    UnaryOperator<ElementoBase> getOnPisada();
    Consumer<String> getOnGameOver();
    void actualizarRejas();
    TableroMemento crearMemento();
    void notificarStateChange(TableroMemento memento, int pushes);
    void notificarVista();
}
