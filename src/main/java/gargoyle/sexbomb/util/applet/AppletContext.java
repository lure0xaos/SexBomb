package gargoyle.sexbomb.util.applet;

import java.net.URL;

public interface AppletContext {
    void showDocument(URL url);

    void showDocument(URL url, String target);
}
