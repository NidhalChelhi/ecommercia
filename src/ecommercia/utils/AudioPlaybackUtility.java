package ecommercia.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlaybackUtility {

    public void playWelcomeAudioAsync() {
        playAudioAsync("/resources/audio/welcome.wav");
    }

    public void playGoodbyeAudioAsync() {
        playAudioAsync("/resources/audio/goodbye.wav");
    }

    private void playAudioAsync(String resourcePath) {
        new Thread(() -> {
            try {
                File audioFile = new File(getClass().getResource(resourcePath).toURI());

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                clip.start();

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
