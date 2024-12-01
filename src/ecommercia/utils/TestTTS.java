package ecommercia.utils;
public class TestTTS {
    public static void main(String[] args) {
        TextToSpeechUtility tts = new TextToSpeechUtility();

        // Example: Speak text using TTS
        tts.speak("Welcome to Ecommercia!");

        // Example: Play a pre-recorded audio file
        tts.playWelcomeAudioAsync();

        // Close resources
        tts.close();
    }
}
