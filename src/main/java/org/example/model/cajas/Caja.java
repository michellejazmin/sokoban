package org.example.model.cajas;

import org.example.model.Coordenada;
import org.example.model.ElementoBase;

public abstract class Caja extends ElementoBase {
    protected Caja(Coordenada coordenada, char simbolo) {
        super(coordenada, simbolo);
    }
}
