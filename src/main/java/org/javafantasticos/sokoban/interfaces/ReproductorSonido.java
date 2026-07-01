package org.javafantasticos.sokoban.interfaces;

public interface ReproductorSonido {
    void reproducirMovimiento();
    void reproducirEmpuje();
    void reproducirItemMoneda();
    void reproducirItemBomba();
    void reproducirItemUndo();
    void reproducirVictoria();
    void reproducirGameOver(String motivo);
    void reproducirUndo();
    void reproducirPasoNivel();
    void reproducirCajaRota();
    void reproducirReja();
    void detenerTodo();
}
