package org.example.model.muros;

import org.example.model.dto.Coordenada;
import org.example.model.ElementoBase;

public final class Pared extends ElementoBase {
    public Pared(Coordenada coordenada) {
        super(coordenada, 'P');
    }
<<<<<<< HEAD
=======

    @Override
    public boolean bloqueaPaso() {
        return true;
    }
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
}
