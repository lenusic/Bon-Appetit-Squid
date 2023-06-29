import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio implements Runnable {
    // to store current position
    Long currentFrame;
    Clip clip;

    // current status of clip
    String status;

    AudioInputStream audioInputStream;
    private String filePath;
    private volatile boolean isPlaying;
    private volatile boolean stopRequested;

    // constructor to initialize streams and clip
    public Audio(String audioFilePath) {
        this.filePath = audioFilePath;
    }

    public void play() {
        isPlaying = true;
        clip.start();

    }

    public void stop() {
        stopRequested = true;
        clip.stop();
        System.out.println("stopped Audio");
    }

    public void run() {
        try {

            // create AudioInputStream object
            audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

            // create clip reference
            clip = AudioSystem.getClip();

            // open audioInputStream to the clip
            clip.open(audioInputStream);

            while (!stopRequested) {
                if (isPlaying) {
                    clip.setFramePosition(0);
                    clip.start();
                    Thread.sleep(clip.getMicrosecondLength() / 1000); // Sleep until the clip finishes playing
                } else {
                    Thread.sleep(10); // Sleep to reduce CPU usage when not playing
                }
            
            }
            clip.stop();
            clip.close();
            audioInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
