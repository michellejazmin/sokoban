package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IReproductorVista;
import org.javafantasticos.sokoban.interfaces.ISuscriptor;
import org.javafantasticos.sokoban.model.Tablero;

import javax.swing.Timer;


public class ReproductorPartida implements IReproductorVista {
    private static final int DELAY_MS = 350;

    private final Tablero tablero;
    private final Grabacion grabacion;
    private final ISuscriptor panelVista;
    private final Timer timer;
    private Runnable onFrameChange;
    private int indice;

    public ReproductorPartida(Tablero tablero, Grabacion grabacion, ISuscriptor panelVista) {
        this.tablero = tablero;
        this.grabacion = grabacion;
        this.panelVista = panelVista;
        this.indice = 0;
        this.timer = new Timer(DELAY_MS, e -> avanzarAutomatico());
    }

    // Callback que se dispara cada vez que cambia el frame o el estado de reproducción.
    public void setOnFrameChange(Runnable callback) {
        this.onFrameChange = callback;
    }

    public void play() {
        if (grabacion.isEmpty()) return;
        // Si está al final, reinicia desde el principio.
        if (indice >= grabacion.size() - 1) {
            indice = 0;
            mostrarFrameActual();
        }
        timer.start();
        notificar();
    }

    public void pausar() {
        timer.stop();
        notificar();
    }

    public void siguiente() {
        timer.stop();
        if (indice < grabacion.size() - 1) {
            indice++;
            mostrarFrameActual();
        } else {
            notificar();
        }
    }

    public void anterior() {
        timer.stop();
        if (indice > 0) {
            indice--;
            mostrarFrameActual();
        } else {
            notificar();
        }
    }

    // Posiciona el reproductor en el primer frame y lo dibuja.
    public void reiniciar() {
        timer.stop();
        indice = 0;
        mostrarFrameActual();
    }

    // Detiene el timer sin redibujar (para soltar el reproductor).
    public void detener() {
        timer.stop();
    }

    public boolean estaReproduciendo() {
        return timer.isRunning();
    }

    public int getIndice() {
        return indice;
    }

    public int getTotalFrames() {
        return grabacion.size();
    }

    private void avanzarAutomatico() {
        if (indice < grabacion.size() - 1) {
            indice++;
            mostrarFrameActual();
        } else {
            pausar(); // Llegó al final.
        }
    }

    private void mostrarFrameActual() {
        tablero.restaurar(grabacion.get(indice).memento());
        panelVista.actualizar(tablero);
        notificar();
    }

    private void notificar() {
        if (onFrameChange != null) onFrameChange.run();
    }
}
