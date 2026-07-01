package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import org.javafantasticos.sokoban.interfaces.IPantallaVictoria;
import java.awt.event.ActionListener;

public class VictoriaPanel extends BaseOverlayPanel implements IPantallaVictoria {
    private static VictoriaPanel instancia;

    private final JButton botonReproducir;
    private final JButton botonVolver;
    private final JButton botonSalir;

    private VictoriaPanel(String mensaje) {
        super("¡Victoria!", mensaje);
        Font bodyFont = UIResources.cargarFuenteRegular(20);

        botonReproducir = crearBoton("Reproducir partida", bodyFont, new Color(0x27, 0xAE, 0x60));
        botonVolver = crearBoton("Volver al menú principal", bodyFont, new Color(0x5D, 0x7B, 0x93));
        botonSalir = crearBoton("Salir", bodyFont, new Color(0xC0, 0x39, 0x2B));

        agregarBotonEn(2, botonReproducir);
        agregarBotonEn(3, botonVolver);
        agregarBotonEn(4, botonSalir);
    }

    public static VictoriaPanel getInstancia(String mensaje) {
        if (instancia == null) instancia = new VictoriaPanel(mensaje);
        return instancia;
    }

    public void setMensaje(String mensaje) { mensajeLabel.setText(mensaje); }
    public void escucharBotonReproducir(ActionListener listener) { botonReproducir.addActionListener(listener); }
    public void escucharBotonVolver(ActionListener listener) { botonVolver.addActionListener(listener); }
    public void escucharBotonSalir(ActionListener listener) { botonSalir.addActionListener(listener); }
}
