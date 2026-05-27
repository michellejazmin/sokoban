package org.example.model.cajas;

import org.example.model.dto.Coordenada;

public final class CajaFragil extends Caja {
    public CajaFragil(Coordenada coordenada) {
        super(coordenada, 'F');
    }
}
