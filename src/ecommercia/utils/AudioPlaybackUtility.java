package ecommercia.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlaybackUtility {

    /**
     * Plays the welcome audio asynchronously.
     */
    public void playWelcomeAudioAsync() {
        playAudioAsync("/resources/audio/welcome.wav");
    }

    /**
     * Plays the goodbye audio asynchronously.
     */
    public void playGoodbyeAudioAsync() {
        playAudioAsync("/resources/audio/goodbye.wav");
    }

    /**
     * Generic method to play an audio file asynchronously.
     *
     * @param resourcePath Path to the audio file resource.
     */
    private void playAudioAsync(String resourcePath) {
        new Thread(() -> {
            try {
                // Load the file from the classpath
                File audioFile = new File(getClass().getResource(resourcePath).toURI());

                // Load the audio file as a clip
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Play the audio
                clip.start();

                // Wait for the audio to finish playing
                while (!clip.isRunning()) {
                    Thread.sleep(10);
                }
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }

                clip.close();
                audioStream.close();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                System.err.println("Error playing audio file: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
