package org.javafantasticos.sokoban.model;

import org.javafantasticos.sokoban.model.player.Jugador;

public class MotorFisico {

    public int mover(Tablero tablero, int dx, int dy) {
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
            tablero.getGrillaSuperior().get(yDestinoCaja).set(xDestinoCaja, elementoAdyacente);

            pushEnEstePaso = true;
        }

        jugador.getCoordenada().setPosX(xDestinoJugador);
        jugador.getCoordenada().setPosY(yDestinoJugador);
        tablero.getGrillaSuperior().get(yJugador).set(xJugador, null);
        tablero.getGrillaSuperior().get(yDestinoJugador).set(xDestinoJugador, jugador);

        if (tablero.getOnPisada() != null) {
            ElementoBase pisoActual = tablero.getGrillaInferior().get(yDestinoJugador).get(xDestinoJugador);
            ElementoBase nuevoPiso = tablero.getOnPisada().apply(pisoActual);
            if (nuevoPiso != pisoActual) {
                tablero.getGrillaInferior().get(yDestinoJugador).set(xDestinoJugador, nuevoPiso);
            }
        }

        int pushes = 0;
        if (pushEnEstePaso) {
            pushes = 1;

            elementoAdyacente.alSerEmpujada(tablero.getOnGameOver());

            ElementoBase terrenoCaja = tablero.getGrillaInferior().get(yDestinoCaja).get(xDestinoCaja);
            if (terrenoCaja.esResbaloso()) {
                pushes += deslizarCaja(tablero, elementoAdyacente, dx, dy);
            }
        }

        tablero.actualizarRejas();

        if (tablero.getOnStateChange() != null) {
            tablero.getOnStateChange().accept(new TableroMemento(tablero), pushes);
        }

        if (elementoAdyacente.esResbaloso()) {
            pushes += mover(tablero, dx, dy);
        }

        tablero.notificarVista();
        return pushes;
    }

    private int deslizarCaja(Tablero tablero, ElementoBase caja, int dx, int dy) {
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
        tablero.getGrillaSuperior().get(ySiguiente).set(xSiguiente, caja);
        tablero.getGrillaSuperior().get(yActual).set(xActual, null);

        int pushes = 1;

        if (!caja.alSerEmpujada(tablero.getOnGameOver())) {
            return pushes;
        }

        ElementoBase terreno = tablero.getGrillaInferior().get(ySiguiente).get(xSiguiente);
        if (terreno.esResbaloso()) {
            pushes += deslizarCaja(tablero, caja, dx, dy);
        }

        return pushes;
    }
}
