package ecommercia.utils;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class TextToSpeechUtility {

    private static final String VOICE_NAME = "kevin16"; // Default built-in FreeTTS voice
    private Voice voice;

    public TextToSpeechUtility() {
        try {
            // Initialize the FreeTTS voice
            System.setProperty("freetts.voices",
                    "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            VoiceManager voiceManager = VoiceManager.getInstance();
            voice = voiceManager.getVoice(VOICE_NAME);

            if (voice == null) {
                throw new IllegalStateException("Voice '" + VOICE_NAME + "' not found.");
            }

            voice.allocate(); // Allocate the voice
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing FreeTTS: " + e.getMessage());
        }
    }

    /**
     * Speaks the given text using FreeTTS.
     *
     * @param text The text to be spoken.
     */
    public void speak(String text) {
        if (voice == null) {
            System.err.println("FreeTTS voice is not initialized.");
            return;
        }

        try {
            voice.speak(text); // Speak the text
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during text-to-speech synthesis: " + e.getMessage());
        }
    }

    /**
     * Plays a pre-recorded audio file.
     */
    public void playWelcomeAudioAsync() {
        new Thread(() -> {
            try {
                // Load the file from the classpath
                File audioFile = new File(getClass().getResource("/resources/audio/welcome.wav").toURI());

                // Load the audio file as a clip
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);

                // Play the audio
                clip.start();

                // Wait for the audio to finish playing
                while (!clip.isRunning())
                    Thread.sleep(10);
                while (clip.isRunning())
                    Thread.sleep(10);

                clip.close();
                audioStream.close();

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                e.printStackTrace();
                System.err.println("Error playing audio file: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Closes the TTS resources.
     */
    public void close() {
        if (voice != null) {
            voice.deallocate(); // Deallocate resources
        }
    }
}
