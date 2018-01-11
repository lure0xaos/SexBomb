package gargoyle.sexbomb.util.applet;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

class AppletContextImpl implements AppletContext {
    @Override
    public void showDocument(URL url) {
        try {
            Desktop.getDesktop().browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDocument(URL url, String target) {
        try {
            Desktop.getDesktop().browse(url.toURI());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
