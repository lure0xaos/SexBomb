module SexBomb.util {
    requires kotlin.stdlib;
    requires kotlin.stdlib.jdk7;
    requires kotlin.stdlib.jdk8;
    requires kotlin.reflect;

    requires java.desktop;
    requires java.prefs;
    requires java.logging;

    exports gargoyle.sexbomb.util.applet;
    exports gargoyle.sexbomb.util.ini;
    exports gargoyle.sexbomb.util.log;
    exports gargoyle.sexbomb.util.res;

    opens gargoyle.sexbomb.util.applet to kotlin.reflect;
}
