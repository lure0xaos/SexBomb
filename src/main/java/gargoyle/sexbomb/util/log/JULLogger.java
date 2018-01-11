package gargoyle.sexbomb.util.log;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class JULLogger extends Logger {
    static {
//        LogManager.getLogManager().reset();
        try {
            LogManager.getLogManager().readConfiguration(JULLogger.class.getResourceAsStream("/logging.properties"));
        } catch (IOException e) {
            //noinspection ProhibitedExceptionThrown
            throw new RuntimeException(e.getMessage(), e);
        }
//        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();
//        java.util.logging.Logger.getGlobal().setLevel(Level.FINEST);
//        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST);
    }

    public JULLogger(LEVEL level) {
        super(level);
    }

    private static Level toLevel(LEVEL level) {
        switch (level) {
            case FATAL:
                return Level.SEVERE;
            case ERROR:
                return Level.SEVERE;
            case WARN:
                return Level.WARNING;
            case INFO:
                return Level.INFO;
            case DEBUG:
                return Level.FINE;
        }
        return Level.SEVERE;
    }

    @Override
    protected void log(StackTraceElement caller, long dateTime, LEVEL level, String message, Throwable throwable) {
        String msg = String.format("[%1$tF %1$tC] [%2$s] %3$s: %4$s%n%5$s", dateTime, level, caller, message, getStackTraceAsString(throwable)).trim();
        LogRecord record = new LogRecord(toLevel(level), message);
        record.setMillis(dateTime);
        record.setSourceClassName(String.format("%s.%s(%s:%d)", caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber()));
        record.setThrown(throwable);
        record.setLoggerName(caller.getClassName());
        java.util.logging.Logger.getLogger(caller.getClassName()).log(record);
    }
}
