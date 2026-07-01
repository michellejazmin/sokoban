package org.javafantasticos.sokoban.controller;

import org.javafantasticos.sokoban.interfaces.ReproductorSonido;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdaptadorClip implements ReproductorSonido {
    private static final String RUTA_SONIDOS = "src/main/resources/sonidos/";
    private static AdaptadorClip instancia;

    private final Map<String, Clip> clips;
    private boolean habilitado;

    private AdaptadorClip() {
        this.clips = new HashMap<>();
        this.habilitado = true;
        precargarSonidos();
    }

    public static AdaptadorClip getInstancia() {
        if (instancia == null) {
            instancia = new AdaptadorClip();
        }
        return instancia;
    }

    private void precargarSonidos() {
        cargarClip("movimiento", "movimiento.wav");
        cargarClip("empuje", "empuje.wav");
        cargarClip("moneda", "moneda.wav");
        cargarClip("bomba", "bomba.wav");
        cargarClip("undo_item", "undo_item.wav");
        cargarClip("victoria", "victoria.wav");
        cargarClip("game_over", "game_over.wav");
        cargarClip("undo", "undo.wav");
        cargarClip("paso_nivel", "paso_nivel.wav");
        cargarClip("caja_rota", "caja_rota.wav");
        cargarClip("reja", "reja.wav");
    }

    private void cargarClip(String clave, String archivo) {
        try {
            File f = new File(RUTA_SONIDOS + archivo);
            if (!f.exists()) {
                System.err.println("[AdaptadorClip] Archivo no encontrado: " + f.getPath());
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
            System.err.println("[AdaptadorClip] Error al cargar " + archivo + ": " + e.getMessage());
        }
    }

    private void reproducir(String clave) {
        if (!habilitado) return;
        Clip clip = clips.get(clave);
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.start();
    }

    @Override
    public void reproducirMovimiento() {
        reproducir("movimiento");
    }

    @Override
    public void reproducirEmpuje() {
        reproducir("empuje");
    }

    @Override
    public void reproducirItemMoneda() {
        reproducir("moneda");
    }

    @Override
    public void reproducirItemBomba() {
        reproducir("bomba");
    }

    @Override
    public void reproducirItemUndo() {
        reproducir("undo_item");
    }

    @Override
    public void reproducirVictoria() {
        reproducir("victoria");
    }

    @Override
    public void reproducirGameOver(String motivo) {
        reproducir("game_over");
    }

    @Override
    public void reproducirUndo() {
        reproducir("undo");
    }

    @Override
    public void reproducirPasoNivel() {
        reproducir("paso_nivel");
    }

    @Override
    public void reproducirCajaRota() {
        reproducir("caja_rota");
    }

    @Override
    public void reproducirReja() {
        reproducir("reja");
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
