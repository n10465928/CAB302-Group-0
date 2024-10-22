package vpm.gui_prototype.services;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundManager {

    /**
     * Method to play sound based on pet type and action (create, interact, leave).
     *
     * @param petType The type of pet (e.g., dog, cat, etc.).
     * @param action  The action (create, interact, leave).
     */
    public static void playSound(String petType, String action) {
        // Construct the relative path to the sound file
        String filePath = String.format("/assets/%s_%s.wav", petType.toLowerCase(), action.toLowerCase());

        // Load the audio file from the classpath
        try (InputStream audioStream = SoundManager.class.getResourceAsStream(filePath);
             BufferedInputStream bufferedIn = new BufferedInputStream(audioStream)) {

            if (bufferedIn == null) {
                System.err.println("Error: Could not find sound file at path: " + filePath);
                return;
            }

            // Convert BufferedInputStream to AudioInputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
