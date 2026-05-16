package org.example.model.muros;

import org.example.model.dto.Coordenada;
import org.example.model.ElementoBase;

public class Pared extends ElementoBase {
    public Pared(Coordenada coordenada) {
        super(coordenada, 'P');
    }
}
