package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.interfaces.IContextoItem;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class Bomba extends ItemPiso {
    private static final String MOTIVO = "Pisaste una bomba.";

    public Bomba(Coordenada coordenada) {
        super(coordenada, 'B');
    }

    @Override
    protected void aplicarEfecto(IContextoItem ctx) {
        ctx.terminarPartida(MOTIVO);
    }
}
