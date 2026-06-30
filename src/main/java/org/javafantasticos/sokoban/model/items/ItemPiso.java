package org.javafantasticos.sokoban.model.items;

import org.javafantasticos.sokoban.model.ElementoBase;
import org.javafantasticos.sokoban.model.dto.Coordenada;
import org.javafantasticos.sokoban.model.suelo.Suelo;

public abstract class ItemPiso extends ElementoBase {

    protected ItemPiso(Coordenada coordenada, char simbolo) {
        super(coordenada, simbolo);
    }

    @Override
    public boolean esOcupable() {
        return true;
    }

    @Override
    public ElementoBase aplicar(ContextoItem ctx) {
        aplicarEfecto(ctx);
        return new Suelo(coordenada);
    }

    protected abstract void aplicarEfecto(ContextoItem ctx);
}
