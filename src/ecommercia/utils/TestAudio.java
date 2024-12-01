package ecommercia.utils;

public class TestAudio {
    public static void main(String[] args) {
        AudioPlaybackUtility audioPlaybackUtility = new AudioPlaybackUtility();

        audioPlaybackUtility.playWelcomeAudioAsync();

        audioPlaybackUtility.playGoodbyeAudioAsync();


    }
}
