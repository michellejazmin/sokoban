package org.example.model.dto;

public final class Coordenada {
    private final int posX;
    private final int posY;

    public Coordenada(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
