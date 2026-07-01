package org.javafantasticos.sokoban.view;

import org.javafantasticos.sokoban.controller.ReproductorPartida;
import org.javafantasticos.sokoban.interfaces.Suscriptor;
import org.javafantasticos.sokoban.interfaces.VistaReplay;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Pantalla de reproducción de la partida grabada.
 * Muestra el tablero animándose y una barra de controles (anterior, play/pausa,
 * siguiente y volver al menú).
 * Patrón Singleton: única pantalla de reproducción durante toda la ejecución.
 * Como el {@link TableroPanel} también es único, esta pantalla lo toma prestado
 * mientras dura la reproducción (ver {@link #cargar}).
 */
public class ReplayPanel extends JPanel implements VistaReplay {
    private static ReplayPanel instancia;

    private final JPanel contenedorTablero;
    private final JLabel progresoLabel;
    private final JButton botonAnterior;
    private final JButton botonPlayPause;
    private final JButton botonSiguiente;
    private final JButton botonVolver;
    private final JButton botonContinuar;

    private ReproductorPartida reproductor;

    private ReplayPanel() {
        super(new BorderLayout());
        setBackground(new Color(0x1E, 0x2A, 0x38));

        contenedorTablero = new JPanel(new GridBagLayout());
        contenedorTablero.setOpaque(false);
        add(contenedorTablero, BorderLayout.CENTER);

        Font uiFont = UIResources.cargarFuenteRegular(14);

        progresoLabel = new JLabel("Movimiento 0 / 0");
        progresoLabel.setFont(uiFont);
        progresoLabel.setForeground(Color.WHITE);

        botonAnterior = crearBoton("← Anterior", uiFont, new Color(0x5D, 0x7B, 0x93));
        botonPlayPause = crearBoton("⏸ Pausa", uiFont, new Color(0x27, 0xAE, 0x60));
        botonSiguiente = crearBoton("Siguiente →", uiFont, new Color(0x5D, 0x7B, 0x93));
        botonVolver = crearBoton("⌂ Volver al menu", uiFont, new Color(0xC0, 0x39, 0x2B));
        botonContinuar = crearBoton("Siguiente nivel →", uiFont, new Color(0x27, 0xAE, 0x60));

        JPanel controles = new JPanel(new GridLayout(3, 1, 16, 8));
        controles.setBackground(new Color(0x16, 0x20, 0x2B));
        controles.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fila1.setOpaque(false);
        fila1.add(progresoLabel);
        controles.add(fila1);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        fila2.setOpaque(false);
        fila2.add(botonAnterior);
        fila2.add(botonPlayPause);
        fila2.add(botonSiguiente);
        controles.add(fila2);

        JPanel fila3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        fila3.setOpaque(false);
        fila3.add(botonVolver);
        fila3.add(botonContinuar);
        controles.add(fila3);

        add(controles, BorderLayout.SOUTH);

        botonAnterior.addActionListener(e -> { if (reproductor != null) reproductor.anterior(); });
        botonSiguiente.addActionListener(e -> { if (reproductor != null) reproductor.siguiente(); });
        botonPlayPause.addActionListener(e -> togglePlay());
    }

    public static ReplayPanel getInstancia() {
        if (instancia == null) instancia = new ReplayPanel();
        return instancia;
    }

    /**
     * Monta el tablero (prestado) y su reproductor para una nueva reproducción.
     * @param mostrarContinuar true para mostrar "Continuar" (nivel completado),
     *                         false para mostrar "Volver al menú" (game over / final).
     * @param alVolver callback para el botón "Volver al menú"
     * @param alContinuar callback para el botón "Continuar" (se ignora si mostrarContinuar=false)
     */
    @Override
    public void cargar(Suscriptor board, ReproductorPartida rep, boolean mostrarContinuar,
                       ActionListener alVolver, ActionListener alContinuar) {
        contenedorTablero.removeAll();
        contenedorTablero.add((TableroPanel) board);
        this.reproductor = rep;

        rep.setOnFrameChange(this::actualizarControles);
        rep.reiniciar();

        Arrays.stream(botonVolver.getActionListeners()).forEach(botonVolver::removeActionListener);
        Arrays.stream(botonContinuar.getActionListeners()).forEach(botonContinuar::removeActionListener);
        botonVolver.addActionListener(alVolver);
        botonContinuar.addActionListener(alContinuar);

        botonVolver.setVisible(!mostrarContinuar);
        botonContinuar.setVisible(mostrarContinuar);

        revalidate();
        repaint();
    }

    private void togglePlay() {
        if (reproductor == null) return;
        if (reproductor.estaReproduciendo()) {
            reproductor.pausar();
        } else {
            reproductor.play();
        }
    }

    private void actualizarControles() {
        if (reproductor == null) return;
        int actual = reproductor.getIndice();
        int ultimo = reproductor.getTotalFrames() - 1;
        progresoLabel.setText("Movimiento " + actual + " / " + Math.max(0, ultimo));
        botonPlayPause.setText(reproductor.estaReproduciendo() ? "⏸ Pausa" : "▶ Reproducir");
        botonAnterior.setEnabled(actual > 0);
        botonSiguiente.setEnabled(actual < ultimo);
    }

    private JButton crearBoton(String texto, Font font, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(font);
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        btn.setMinimumSize(new Dimension(150, 32));
        return btn;
    }
}
