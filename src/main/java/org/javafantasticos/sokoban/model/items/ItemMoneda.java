package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.interfaces.IContextoItem;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class ItemMoneda extends ItemPiso {
    private static final int BONUS = 200;

    public ItemMoneda(Coordenada coordenada) {
        super(coordenada, 'M');
    }

    @Override
    protected void aplicarEfecto(IContextoItem ctx) {
        ctx.sumarBonus(BONUS);
    }
}
