package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.interfaces.IContextoItem;
import org.javafantasticos.sokoban.model.dto.Coordenada;

public final class ItemUndoExtra extends ItemPiso {

    public ItemUndoExtra(Coordenada coordenada) {
        super(coordenada, 'U');
    }

    @Override
    protected void aplicarEfecto(IContextoItem ctx) {
        ctx.sumarUndoExtra();
    }
}
