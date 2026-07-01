package org.javafantasticos.sokoban.interfaces;

import java.awt.*;

public interface IMovimientos{
    void arriba();
    void abajo();
    void derecha();
    void izquierda();
    void registrarEn(Component componente);
    void desregistrarDe(Component componente);
}
