import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio implements Runnable {
    // to store current position
    Long currentFrame;
    Clip clip;

    // current status of clip
    String status;

    AudioInputStream audioInputStream;
    private String filePath = "resources/the-squid-song.wav";
    private volatile boolean isPlaying;

    // constructor to initialize streams and clip
    public Audio(String audioFilePath) {
        this.filePath = audioFilePath;
    }

    public void play() {
        isPlaying = true;
    }

    public void stop() {
        isPlaying = false;
    }

    public void run() {
        try {
            // throws UnsupportedAudioFileException,
            // IOException, LineUnavailableException

            // create AudioInputStream object
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            // create clip reference
            clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            // clip.loop(Clip.LOOP_CONTINUOUSLY);

            while (true) {
                if (isPlaying) {
                    clip.setFramePosition(0);
                    clip.start();
                    Thread.sleep(clip.getMicrosecondLength() / 1000); // Sleep until the clip finishes playing
                } else {
                    Thread.sleep(10); // Sleep to reduce CPU usage when not playing
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
