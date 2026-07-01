package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.interfaces.ContextoItem;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class ItemMoneda extends ItemPiso {
    private static final int BONUS = 200;

    public ItemMoneda(Coordenada coordenada) {
        super(coordenada, 'M');
    }

    @Override
    protected void aplicarEfecto(ContextoItem ctx) {
        ctx.sumarBonus(BONUS);
    }
}
