package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.IReproductorSonido;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorSonido implements IReproductorSonido {
    private static final String RUTA_SONIDOS = "src/main/resources/sonidos/";
    private static GestorSonido instancia;

    private final Map<String, Clip> clips;
    private boolean habilitado;

    private GestorSonido() {
        this.clips = new HashMap<>();
        this.habilitado = true;
        precargarSonidos();
    }

    public static GestorSonido getInstancia() {
        if (instancia == null) {
            instancia = new GestorSonido();
        }
        return instancia;
    }

    private void precargarSonidos() {
        List<String> eventos = List.of(
                MOVIMIENTO, EMPUJE, MONEDA, BOMBA, UNDO_ITEM,
                VICTORIA, GAME_OVER, UNDO, PASO_NIVEL, CAJA_ROTA, REJA
        );
        for (String clave : eventos) {
            cargarClip(clave, clave + ".wav");
        }
    }

    private void cargarClip(String clave, String archivo) {
        try {
            File f = new File(RUTA_SONIDOS + archivo);
            if (!f.exists()) {
                System.err.println("[GestorSonido] Archivo no encontrado: " + f.getPath());
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(f);
            AudioFormat sourceFormat = audioStream.getFormat();

            if (sourceFormat.getSampleSizeInBits() > 16) {
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        sourceFormat.getSampleRate(),
                        16,
                        sourceFormat.getChannels(),
                        sourceFormat.getChannels() * 2,
                        sourceFormat.getSampleRate(),
                        sourceFormat.isBigEndian()
                );
                audioStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);
            }

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clips.put(clave, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("[GestorSonido] Error al cargar " + archivo + ": " + e.getMessage());
        }
    }

    @Override
    public void reproducir(String evento) {
        if (!habilitado) return;
        Clip clip = clips.get(evento);
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void detenerTodo() {
        for (Clip clip : clips.values()) {
            if (clip.isRunning()) {
                clip.stop();
            }
        }
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public boolean isHabilitado() {
        return habilitado;
    }
}
