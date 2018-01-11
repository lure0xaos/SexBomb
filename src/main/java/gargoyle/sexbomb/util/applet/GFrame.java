package gargoyle.sexbomb.util.applet;

import gargoyle.sexbomb.util.log.Log;
import gargoyle.sexbomb.util.res.Resources;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

final class GFrame extends JFrame implements WindowListener, AppletStub, AppletContext {
    private static final String STR_READY = "Ready";
    private final G0Applet applet;
    private final Resources resources;
    private final Map<String, String> parameters = new HashMap<>();
    private final JLabel status;
    private final Map<String, InputStream> streams = new HashMap<>();

    public GFrame(G0Applet applet, Resources resources, String[] args) {
        this.applet = applet;
        this.resources = resources;
        applet.application = this;
        applet.setStub(this);
        setArgs(args);
        setTitle(applet.getTitle());
        setLayout(new BorderLayout());
        add(applet, BorderLayout.CENTER);
        status = new JLabel(STR_READY);
        status.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        add(status, BorderLayout.SOUTH);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);
        Dimension
                size =
                new Dimension(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize());
        size.width >>= 1;
        size.height >>= 1;
        applet.setPreferredSize(size);
        pack();
        setLocationRelativeTo(null);
    }

    void exit() {
        applet.destroy();
        SwingUtilities.invokeLater(() -> {
            setVisible(false);
            dispose();
        });
    }

    @Override
    public AudioClip getAudioClip(URL url) {
        return resources.load(AudioClip.class, url);
    }

    @Override
    public Image getImage(URL url) {
        return resources.load(Image.class, url);
    }

    @Override
    public Applet getApplet(String name) {
        return applet;
    }

    @Override
    public Enumeration<Applet> getApplets() {
        return Collections.enumeration(Arrays.asList(new Applet[]{applet}));
    }

    @Override
    public void showDocument(URL url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(url.toURI());
            }
        } catch (IOException | URISyntaxException e) {
            Log.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void showDocument(URL url, String target) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(url.toURI());
            }
        } catch (IOException | URISyntaxException e) {
            Log.warn(e.getLocalizedMessage());
        }
    }

    @Override
    public void showStatus(String status) {
        if (isVisible()) {
            this.status.setText(status);
        }
    }

    @Override
    public void setStream(String key, InputStream stream) {
        streams.put(key, stream);
    }

    @Override
    public InputStream getStream(String key) {
        return streams.get(key);
    }

    @Override
    public Iterator<String> getStreamKeys() {
        return streams.keySet().iterator();
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL(new File(System.getProperty("user.dir", ".")).getCanonicalFile().toURI().toURL(), "");
        } catch (IOException e) {
            Log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public URL getCodeBase() {
        try {
            return getClass().getProtectionDomain().getCodeSource().getLocation();
        } catch (RuntimeException e) {
            Log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return this;
    }

    @Override
    public void appletResize(int width, int height) {
        synchronized (getTreeLock()) {
            Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            Rectangle size = new Rectangle(getLocation(), applet.getMinimumSize());
            if (width > size.width) {
                size.width = width;
            }
            if (height > size.height) {
                size.height = height;
            }
            if (size.width > screen.width) {
                size.width = screen.width;
            }
            if (size.height > screen.height) {
                size.height = screen.height;
            }
            int x = screen.x + screen.width - size.width;
            if (size.x > x) {
                size.x = x;
            }
            int y = screen.y + screen.height - size.height;
            if (size.y > y) {
                size.y = y;
            }
            applet.setPreferredSize(size.getSize());
            applet.setSize(size.getSize());
            setLocation(size.getLocation());
            pack();
        }
    }

    private void setArgs(String[] args) {
        for (String arg : args) {
            String[] split = arg.split("=", 2);
            parameters.put(split[0], split[1]);
        }
    }

    public void showMe() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            applet.init();
        });
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            if (applet.canExit()) {
                applet.exit();
            } else {
                if (!isVisible()) {
                    setVisible(true);
                }
            }
        } catch (RuntimeException e1) {
            applet.error(e1);
            dispose();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
        applet.start();
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        applet.stop();
    }
}
