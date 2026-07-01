package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.interfaces.ContextoItem;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class ItemUndoExtra extends ItemPiso {

    public ItemUndoExtra(Coordenada coordenada) {
        super(coordenada, 'U');
    }

    @Override
    protected void aplicarEfecto(ContextoItem ctx) {
        ctx.sumarUndoExtra();
    }
}
