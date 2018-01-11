package gargoyle.sexbomb.util.applet;

import gargoyle.sexbomb.util.res.Resources;

public abstract class GXApplet extends G0Applet {
    protected GXApplet(Resources resources) {
        super(resources);
    }

    protected static void run(Class<? extends GXApplet> clazz, String[] args) {
        _run(clazz, args);
    }

    protected abstract void doDestroy();

    protected abstract void doInit();

    protected abstract void doStart();

    protected abstract void doStop();

    @Override
    public final synchronized void init() {
        setFocusable(true);
        try {
            doInit();
        } catch (RuntimeException e) {
            error(e);
        }
    }

    @Override
    public final synchronized void start() {
        try {
            doStart();
        } catch (RuntimeException e) {
            error(e);
        }
    }

    @Override
    public final synchronized void stop() {
        try {
            doStop();
        } catch (RuntimeException e) {
            error(e);
        }
    }

    @Override
    public final synchronized void destroy() {
        try {
            doDestroy();
        } catch (RuntimeException e) {
            error(e);
        }
    }
}
