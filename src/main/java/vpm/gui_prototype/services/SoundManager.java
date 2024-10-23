package vpm.gui_prototype.services;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Manages sound playback for pet-related actions.
 */
public class SoundManager {

    /**
     * Plays a sound based on the pet type and action (create, interact, leave).
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
                return; // Exit if the sound file cannot be found
            }

            // Convert BufferedInputStream to AudioInputStream
            AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip(); // Obtain a clip for playback
            clip.open(ais); // Open the audio input stream
            clip.start(); // Start playing the sound
        } catch (UnsupportedAudioFileException e) {
            // Handle the case where the audio file format is not supported
            System.err.println("Error: Unsupported audio file format for path: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            // Handle IO exceptions that may occur while reading the audio file
            System.err.println("Error: IO exception while playing sound from path: " + filePath);
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            // Handle the case where a line for playback is unavailable
            System.err.println("Error: Line unavailable for audio playback.");
            e.printStackTrace();
        }
    }
}
