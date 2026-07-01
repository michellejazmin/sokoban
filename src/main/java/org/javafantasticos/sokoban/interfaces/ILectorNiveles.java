package org.javafantasticos.sokoban.interfaces;


import java.util.List;

public interface ILectorNiveles {
    List<List<String>> extraerNivelesTexto();
    void setRutaArchivo(String ruta);
}