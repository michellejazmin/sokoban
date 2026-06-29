package org.javafantasticos.sokoban.model.cajas;

import org.javafantasticos.sokoban.model.dto.Coordenada;

import java.util.function.Consumer;

public final class CajaFragil extends Caja {
    private int ttl;
    public CajaFragil(Coordenada coordenada) {
        super(coordenada, 'F');
        this.ttl = 25;
    }

    public boolean sinVida(){
        return this.ttl==0;
    }

    public int getTtl() {
        return this.ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void reducirVida() {
        if (this.sinVida()){
            System.out.println("Sin Vida");
        }
        this.ttl--;
    }

    @Override
    public boolean alSerEmpujada(Consumer<String> onGameOver) {
        reducirVida();
        if (sinVida()) {
            if (onGameOver != null) {
                onGameOver.accept("La caja frágil se ha roto :(. Sokoban desempleado.");
            }
            return false;
        }
        return true;
    }
}
