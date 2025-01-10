package com.eletrocp.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

    private Clip clip;
    
    public static final Sound musicBackground = new Sound("/music.wav");
    public static final Sound hurt = new Sound("/hurt.wav");
    
    private Sound(String name) {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getResource(name));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            System.out.println("Sound loaded: " + name);
        } catch (Exception e) {
            System.out.println("Error loading sound: " + name);
            e.printStackTrace();
        }
    }
    
    public void play() {
        try {
            new Thread() {
                public void run() {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.setFramePosition(0); // Reinicia o clip
                    clip.start();
                    System.out.println("Playing sound");
                }
            }.start();
        } catch (Exception e) {
            System.out.println("Error playing sound");
            e.printStackTrace();
        }
    }
    
    public void loop() {
        try {
            new Thread() {
                public void run() {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    System.out.println("Looping sound");
                }
            }.start();
        } catch (Exception e) {
            System.out.println("Error looping sound");
            e.printStackTrace();
        }
    }
}
