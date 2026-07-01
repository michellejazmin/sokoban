package org.javafantasticos.sokoban.interfaces;

public interface IReproductorVista {
    void play();
    void pausar();
    void anterior();
    void siguiente();
    void reiniciar();
    void detener();
    boolean estaReproduciendo();
    int getIndice();
    int getTotalFrames();
    void setOnFrameChange(Runnable callback);
}
