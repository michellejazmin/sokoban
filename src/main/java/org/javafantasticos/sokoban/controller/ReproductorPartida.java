package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.model.Tablero;
import org.javafantasticos.sokoban.view.TableroPanel;

import javax.swing.Timer;

/**
 * Reproduce una {@link Grabacion} restaurando, frame a frame, los mementos sobre
 * el mismo Tablero en el que se grabó la partida y repintando un TableroPanel.
 *
 * La reproducción es automática (un {@link Timer} de Swing avanza los frames),
 * pero puede pausarse y avanzarse/retrocederse manualmente.
 */
public class ReproductorPartida {
    private static final int DELAY_MS = 350;

    private final Tablero tablero;
    private final Grabacion grabacion;
    private final TableroPanel tableroPanel;
    private final Timer timer;
    private Runnable onFrameChange;
    private int indice;

    public ReproductorPartida(Tablero tablero, Grabacion grabacion, TableroPanel tableroPanel) {
        this.tablero = tablero;
        this.grabacion = grabacion;
        this.tableroPanel = tableroPanel;
        this.indice = 0;
        this.timer = new Timer(DELAY_MS, e -> avanzarAutomatico());
    }

    /** Callback que se dispara cada vez que cambia el frame o el estado de reproducción. */
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

    /** Posiciona el reproductor en el primer frame y lo dibuja. */
    public void reiniciar() {
        timer.stop();
        indice = 0;
        mostrarFrameActual();
    }

    /** Detiene el timer sin redibujar (para soltar el reproductor). */
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
            pausar(); // llegó al final
        }
    }

    private void mostrarFrameActual() {
        tablero.restaurar(grabacion.get(indice).memento());
        tableroPanel.actualizar(tablero);
        notificar();
    }

    private void notificar() {
        if (onFrameChange != null) onFrameChange.run();
    }
}
