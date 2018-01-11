package gargoyle.sexbomb.util.applet;

import java.io.Closeable;
import java.io.IOException;

public interface AudioClip extends Closeable {

    void play();

    void loop();

    void stop();

    @Override
    default void close() {
    }
}
