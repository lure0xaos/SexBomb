package gargoyle.sexbomb.util.applet;

import gargoyle.sexbomb.util.log.Log;
import gargoyle.sexbomb.util.res.Resources;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.net.URL;

public abstract class G0Applet extends JApplet {
    private static final String STR_EXIT = "Exit?";
    protected final Resources resources;
    GFrame application;

    protected G0Applet(Resources resources) {
        this.resources = resources;
        application = null;
        setFocusable(true);
    }

    static void _run(Class<? extends G0Applet> clazz, String[] args) {
        try {
            changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), null);
            G0Applet applet = clazz.newInstance();
            new GFrame(applet, applet.resources, args).showMe();
            changeLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), applet);
        } catch (InstantiationException | IllegalAccessException e) {
            Log.fatal(e.getLocalizedMessage(), e);
        }
    }

    private static void changeLookAndFeel(String laf, Component component) {
        try {
            boolean decor = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (component != null) {
                decor &= SwingUtilities.getRootPane(component).getWindowDecorationStyle() != JRootPane.NONE;
            }
            UIManager.setLookAndFeel(laf);
            JFrame.setDefaultLookAndFeelDecorated(decor);
            JDialog.setDefaultLookAndFeelDecorated(decor);
            if (component != null) {
                Window frame = SwingUtilities.getWindowAncestor(component);
                if (!frame.isDisplayable()) {
                    if (frame instanceof Frame) {
                        ((Frame) frame).setUndecorated(!decor);
                    }
                    if (frame instanceof Dialog) {
                        ((Dialog) frame).setUndecorated(!decor);
                    }
                }
            }
            if (component != null) {
                SwingUtilities.updateComponentTreeUI(component);
            }
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            Log.warn(e.getLocalizedMessage(), e);
        }
    }

    private boolean ask(String message) {
        return JOptionPane.showConfirmDialog(this, message, getTitle(), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public boolean canExit() {
        return isApplication() && ask(STR_EXIT);
    }

    protected void changeLookAndFeel(String laf) {
        changeLookAndFeel(laf, this);
    }

    public final void error(Throwable message) {
        JOptionPane.showMessageDialog(this, String.format("%s: %s", message.getClass().getSimpleName(), message.getLocalizedMessage()), getTitle(), JOptionPane.ERROR_MESSAGE);
        Log.error(message.getLocalizedMessage(), message);
    }

    public void exit() {
        if (application != null) {
            application.exit();
        }
    }

    protected final Frame getRootFrame() {
        Component parent = SwingUtilities.getRoot(this);
        if (parent instanceof Frame) {
            return (Frame) parent;
        }
        return null;
    }

    public String getTitle() {
        Component parent = SwingUtilities.getRoot(this);
        if (parent instanceof Frame) {
            return ((Frame) parent).getTitle();
        }
        return getClass().getSimpleName();
    }

    protected final boolean isApplication() {
        return application != null;
    }

    public final void showDocument(URL url) {
        getAppletContext().showDocument(url);
    }

    protected final void showDocument(URL url, String target) {
        getAppletContext().showDocument(url, target);
    }
}
