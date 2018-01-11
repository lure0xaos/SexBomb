package gargoyle.sexbomb.util.applet;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import java.awt.Image;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

public class JApplet extends JPanel implements Applet {
    private final AppletContext context = new AppletContextImpl();
    private AppletStub stub;

    public void appletResize(int width, int height) {
        stub.appletResize(width, height);
    }

    public Applet getApplet(String name) {
        return stub.getApplet(name);
    }

    public AppletContext getAppletContext() {
        return context;
    }

    public Enumeration<Applet> getApplets() {
        return stub.getApplets();
    }

    public AudioClip getAudioClip(URL url) {
        return stub.getAudioClip(url);
    }

    public URL getCodeBase() {
        return stub.getCodeBase();
    }

    public URL getDocumentBase() {
        return stub.getDocumentBase();
    }

    public Image getImage(URL url) {
        return stub.getImage(url);
    }

    public String getParameter(String name) {
        return stub.getParameter(name);
    }

    public InputStream getStream(String key) {
        return stub.getStream(key);
    }

    public Iterator<String> getStreamKeys() {
        return stub.getStreamKeys();
    }

    public void setStream(String key, InputStream stream) {
        stub.setStream(key, stream);
    }

    @Override
    public void setStub(AppletStub stub) {
        this.stub = stub;
    }

    @Override
    public void init() {
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
    }

    protected void showDocument(URL url) {
        stub.showDocument(url);
    }

    protected void showDocument(URL url, String target) {
        stub.showDocument(url, target);
    }

    protected void showStatus(String status) {
        stub.showStatus(status);
    }

    protected void setJMenuBar(JMenuBar bar) {
        super.getRootPane().setJMenuBar(bar);
    }
}
