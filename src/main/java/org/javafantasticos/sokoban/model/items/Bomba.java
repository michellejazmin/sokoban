package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.interfaces.ContextoItem;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class Bomba extends ItemPiso {
    private static final String MOTIVO = "Pisaste una bomba.";

    public Bomba(Coordenada coordenada) {
        super(coordenada, 'B');
    }

    @Override
    protected void aplicarEfecto(ContextoItem ctx) {
        ctx.terminarPartida(MOTIVO);
    }
}
