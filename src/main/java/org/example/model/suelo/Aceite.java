package org.example.model.suelo;

import org.example.model.dto.Coordenada;
import org.example.model.ElementoBase;

public class Aceite extends ElementoBase {
    public Aceite(Coordenada coordenada) {
        super(coordenada, 'A');
    }
<<<<<<< HEAD
=======

    @Override
    public boolean esResbaloso() {
        return true;
    }
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
}
