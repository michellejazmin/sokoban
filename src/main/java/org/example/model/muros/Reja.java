package org.example.model.muros;

import org.example.enumerados.Estado;
import org.example.model.ElementoBase;
import org.example.model.dto.Coordenada;

public final class Reja extends ElementoBase {
    private Estado estado;
    public Reja (Coordenada coordenada){
        super(coordenada,'R');
        this.estado=Estado.CERRADO;

    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Estado getEstado() {
        return estado;
    }
<<<<<<< HEAD
=======

    @Override
    public boolean bloqueaPaso() {
        return estado == Estado.CERRADO;
    }
>>>>>>> 9898165 (modificaciones estructurales para la construccion de un TPO extraordinario)
}
