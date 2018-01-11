package gargoyle.sexbomb.util.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

abstract class Logger {
    private final LEVEL level;

    Logger(LEVEL level) {
        this.level = level;
    }

    protected static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        try (StringWriter out = new StringWriter()) {
            throwable.printStackTrace(new PrintWriter(out));
            return out.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public final boolean canLog(LEVEL level) {
        return this.level == null || level == null || level.canLog(this.level);
    }

    final void log(StackTraceElement caller, long date, Throwable throwable) {
        String message = getStackTraceAsString(throwable);
        log(caller, date, LEVEL.DEBUG, String.format("%s: %s\n%s", throwable.getClass().getName(), throwable.getLocalizedMessage(), message), throwable);
    }

    protected abstract void log(StackTraceElement caller, long dateTime, LEVEL level, String message, Throwable throwable);
}
