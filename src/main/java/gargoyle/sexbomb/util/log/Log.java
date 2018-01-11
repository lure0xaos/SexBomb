package gargoyle.sexbomb.util.log;

import java.util.Date;

@SuppressWarnings("LawOfDemeter")
public final class Log {
    private static final Logger[] LOGGERS = {
            new JULLogger(null)
    };

    private Log() {
    }

    public static void debug(String message) {
        log(message, LEVEL.DEBUG);
    }

    public static void error(String message) {
        log(message, LEVEL.ERROR);
    }

    public static void error(String message, Throwable throwable) {
        log(message, throwable, LEVEL.ERROR);
    }

    public static void fatal(String message) {
        log(message, LEVEL.FATAL);
    }

    public static void fatal(String message, Throwable throwable) {
        log(message, throwable, LEVEL.FATAL);
    }

    private static Logger findLogger(LEVEL level) {
        for (Logger logger : LOGGERS) {
            if (logger.canLog(level)) {
                return logger;
            }
        }
        return SystemLogger.getSystemLogger();
    }

    private static StackTraceElement getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String name = Log.class.getPackage().getName();
        for (int i = 1, stackTraceLength = stackTrace.length; i < stackTraceLength; i++) {
            StackTraceElement element = stackTrace[i];
            String className = element.getClassName();
            if (!className.startsWith(name)) {
                return element;
            }
        }
        return stackTrace[0];
    }

    public static void info(String message) {
        log(message, LEVEL.INFO);
    }

    private static void log(String message, LEVEL level) {
        findLogger(level).log(getCaller(), new Date().getTime(), level, message, null);
    }

    public static void log(String message, Throwable throwable) {
        log(message, throwable, LEVEL.FATAL);
    }

    private static void log(String message, Throwable throwable, LEVEL level) {
        Logger logger = findLogger(level);
        logger.log(getCaller(), new Date().getTime(), level, message == null ? String.format("%s: %s", throwable.getClass(), throwable.getLocalizedMessage()) : message, throwable);
    }

    public static void warn(String message) {
        log(message, LEVEL.WARN);
    }

    public static void warn(String msg, Throwable throwable) {
        log(msg, throwable, LEVEL.WARN);
    }
}
