package org.javafantasticos.sokoban.interfaces;

public interface IReproductorSonido {
    String MOVIMIENTO = "movimiento";
    String EMPUJE = "empuje";
    String MONEDA = "moneda";
    String BOMBA = "bomba";
    String UNDO_ITEM = "undo_item";
    String VICTORIA = "victoria";
    String GAME_OVER = "game_over";
    String UNDO = "undo";
    String PASO_NIVEL = "paso_nivel";
    String CAJA_ROTA = "caja_rota";
    String REJA = "reja";

    void reproducir(String evento);
    void detenerTodo();
}
