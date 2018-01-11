package gargoyle.sexbomb.util.applet;

public interface Applet {
    void setStub(AppletStub stub);

    void init();

    void start();

    void stop();

    void destroy();
}
