package gargoyle.sexbomb.util.applet;

import gargoyle.sexbomb.util.log.Log;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public interface AppletStub {

    AudioClip getAudioClip(URL url);

    Image getImage(URL url);

    Applet getApplet(String name);

    Enumeration<Applet> getApplets();

    void showDocument(URL url);

    void showDocument(URL url, String target);

    void showStatus(String status);

    void setStream(String key, InputStream stream);

    InputStream getStream(String key);

    Iterator<String> getStreamKeys();

    URL getDocumentBase();

    URL getCodeBase();

    String getParameter(String name);

    AppletContext getAppletContext();

    void appletResize(int width, int height);
}
