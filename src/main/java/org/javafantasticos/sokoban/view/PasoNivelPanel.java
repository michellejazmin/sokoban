package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import org.javafantasticos.sokoban.interfaces.IPantallaPasoNivel;
import java.awt.event.ActionListener;

public class PasoNivelPanel extends BaseOverlayPanel implements IPantallaPasoNivel {
    private static PasoNivelPanel instancia;

    private final JButton botonSiguiente;
    private final JButton botonReproducir;
    private final JButton botonVolver;

    private PasoNivelPanel(String mensaje) {
        super("¡Nivel completado!", mensaje);
        Font bodyFont = UIResources.cargarFuenteRegular(20);

        botonSiguiente = crearBoton("Siguiente nivel →", bodyFont, new Color(0x27, 0xAE, 0x60));
        botonReproducir = crearBoton("Reproducir partida", bodyFont, new Color(0x29, 0x80, 0xB9));
        botonVolver = crearBoton("Volver al menú principal", bodyFont, new Color(0x5D, 0x7B, 0x93));

        agregarBotonEn(2, botonSiguiente);
        agregarBotonEn(3, botonReproducir);
        agregarBotonEn(4, botonVolver);
    }

    public static PasoNivelPanel getInstancia(String mensaje) {
        if (instancia == null) instancia = new PasoNivelPanel(mensaje);
        return instancia;
    }

    public void setMensaje(String mensaje) { mensajeLabel.setText(mensaje); }
    public void escucharBotonSiguiente(ActionListener listener) { botonSiguiente.addActionListener(listener); }
    public void escucharBotonReproducir(ActionListener listener) { botonReproducir.addActionListener(listener); }
    public void escucharBotonVolver(ActionListener listener) { botonVolver.addActionListener(listener); }
}
