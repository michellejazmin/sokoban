package org.example.model.suelo;

import org.example.model.dto.Coordenada;
import org.example.model.ElementoBase;

public class Aceite extends ElementoBase {
    public Aceite(Coordenada coordenada) {
        super(coordenada, 'A');
    }

    @Override
    public boolean esResbaloso() {
        return true;
    }
}
