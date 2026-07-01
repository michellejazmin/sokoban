package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.interfaces.ITableroFisico;
import org.javafantasticos.sokoban.model.player.Jugador;

public class MotorFisico {

    public int mover(ITableroFisico tablero, int dx, int dy) {
        Jugador jugador = tablero.getJugador();
        int xJugador = jugador.getCoordenada().getPosX();
        int yJugador = jugador.getCoordenada().getPosY();
        int xDestinoJugador = xJugador + dx;
        int yDestinoJugador = yJugador + dy;

        ElementoBase elementoAdyacente = tablero.getElemento(xDestinoJugador, yDestinoJugador);

        if (elementoAdyacente.bloqueaPaso()) return 0;

        boolean pushEnEstePaso = false;
        int xDestinoCaja = -1;
        int yDestinoCaja = -1;

        if (elementoAdyacente.esMovible()) {
            xDestinoCaja = xDestinoJugador + dx;
            yDestinoCaja = yDestinoJugador + dy;
            ElementoBase destinoCaja = tablero.getElemento(xDestinoCaja, yDestinoCaja);

            if (!destinoCaja.esOcupable()) return 0;

            elementoAdyacente.getCoordenada().setPosX(xDestinoCaja);
            elementoAdyacente.getCoordenada().setPosY(yDestinoCaja);
            tablero.asignarSuperior(xDestinoCaja, yDestinoCaja, elementoAdyacente);

            pushEnEstePaso = true;
        }

        jugador.getCoordenada().setPosX(xDestinoJugador);
        jugador.getCoordenada().setPosY(yDestinoJugador);
        tablero.limpiarSuperior(xJugador, yJugador);
        tablero.asignarSuperior(xDestinoJugador, yDestinoJugador, jugador);

        if (tablero.getOnPisada() != null) {
            ElementoBase pisoActual = tablero.getInferior(xDestinoJugador, yDestinoJugador);
            ElementoBase nuevoPiso = tablero.getOnPisada().apply(pisoActual);
            if (nuevoPiso != pisoActual) {
                tablero.asignarInferior(xDestinoJugador, yDestinoJugador, nuevoPiso);
            }
        }

        int pushes = 0;
        if (pushEnEstePaso) {
            pushes = 1;

            elementoAdyacente.alSerEmpujada(tablero.getOnGameOver());

            ElementoBase terrenoCaja = tablero.getInferior(xDestinoCaja, yDestinoCaja);
            if (terrenoCaja.esResbaloso()) {
                pushes += deslizarCaja(tablero, elementoAdyacente, dx, dy);
            }
        }

        tablero.actualizarRejas();

        tablero.notificarStateChange(tablero.crearMemento(), pushes);

        if (elementoAdyacente.esResbaloso()) {
            pushes += mover(tablero, dx, dy);
        }

        tablero.notificarVista();
        return pushes;
    }

    private int deslizarCaja(ITableroFisico tablero, ElementoBase caja, int dx, int dy) {
        int xActual = caja.getCoordenada().getPosX();
        int yActual = caja.getCoordenada().getPosY();
        int xSiguiente = xActual + dx;
        int ySiguiente = yActual + dy;

        ElementoBase destino = tablero.getElemento(xSiguiente, ySiguiente);

        if (!destino.esOcupable()) {
            return 0;
        }

        caja.getCoordenada().setPosX(xSiguiente);
        caja.getCoordenada().setPosY(ySiguiente);
        tablero.asignarSuperior(xSiguiente, ySiguiente, caja);
        tablero.limpiarSuperior(xActual, yActual);

        int pushes = 1;

        if (!caja.alSerEmpujada(tablero.getOnGameOver())) {
            return pushes;
        }

        ElementoBase terreno = tablero.getInferior(xSiguiente, ySiguiente);
        if (terreno.esResbaloso()) {
            pushes += deslizarCaja(tablero, caja, dx, dy);
        }

        return pushes;
    }
}
