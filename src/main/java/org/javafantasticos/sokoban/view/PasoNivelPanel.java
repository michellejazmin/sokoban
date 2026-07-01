package org.javafantasticos.sokoban.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class PasoNivelPanel extends JPanel {
    private static PasoNivelPanel instancia;

    private final JLabel tituloLabel;
    private final JLabel mensajeLabel;
    private final JButton botonSiguiente;
    private final JButton botonReproducir;
    private final JButton botonVolver;
    private final BufferedImage backgroundImage;

    private PasoNivelPanel(String mensaje) {
        super();
        this.setLayout(new GridBagLayout());

        backgroundImage = UIResources.cargarFondo();
        if (backgroundImage == null) this.setBackground(new Color(110, 197, 226));

        Font titleFont = UIResources.cargarFuenteBold(48);
        Font bodyFont  = UIResources.cargarFuenteRegular(20);

        tituloLabel = new JLabel("¡Nivel completado!");
        tituloLabel.setFont(titleFont);
        tituloLabel.setForeground(Color.WHITE);

        mensajeLabel = new JLabel(mensaje);
        mensajeLabel.setFont(bodyFont);
        mensajeLabel.setForeground(Color.WHITE);

        botonSiguiente = crearBoton("Siguiente nivel →", bodyFont, new Color(0x27, 0xAE, 0x60));
        botonReproducir = crearBoton("Reproducir partida", bodyFont, new Color(0x29, 0x80, 0xB9));
        botonVolver = crearBoton("Volver al menú principal", bodyFont, new Color(0x5D, 0x7B, 0x93));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0; add(tituloLabel, gbc);
        gbc.gridy = 1; add(mensajeLabel, gbc);
        gbc.gridy = 2; add(botonSiguiente, gbc);
        gbc.gridy = 3; add(botonReproducir, gbc);
        gbc.gridy = 4; add(botonVolver, gbc);
    }

    public static PasoNivelPanel getInstancia(String mensaje) {
        if (instancia == null) instancia = new PasoNivelPanel(mensaje);
        return instancia;
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
        return btn;
    }

    public void setMensaje(String mensaje) { mensajeLabel.setText(mensaje); }
    public void escucharBotonSiguiente(ActionListener listener) { botonSiguiente.addActionListener(listener); }
    public void escucharBotonReproducir(ActionListener listener) { botonReproducir.addActionListener(listener); }
    public void escucharBotonVolver(ActionListener listener) { botonVolver.addActionListener(listener); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIResources.dibujarFondoMosaico(g, backgroundImage, getWidth(), getHeight());
    }
}
