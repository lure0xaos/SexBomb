package gargoyle.sexbomb.util.log;

final class SystemLogger extends StreamLogger {
    private static final Logger LOGGER = new SystemLogger();

    private SystemLogger() {
    }

    static Logger getSystemLogger() {
        return LOGGER;
    }
}
