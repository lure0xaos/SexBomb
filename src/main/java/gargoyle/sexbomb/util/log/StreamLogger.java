package gargoyle.sexbomb.util.log;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class StreamLogger extends Logger {
    private final PrintWriter writer;

    StreamLogger() {
        this(Charset.defaultCharset());
    }

    private StreamLogger(Charset charset) {
        this(new OutputStreamWriter(System.err, charset));
    }

    private StreamLogger(Writer writer) {
        super(null);
        this.writer = new PrintWriter(writer);
    }

    @Override
    protected final synchronized void log(StackTraceElement caller, long dateTime, LEVEL level, String message, Throwable throwable) {
        String msg = String.format("[%1$tF %1$tC] [%2$s] %3$s: %4$s%n%5$s", dateTime, level,
                String.format("%s.%s(%s:%d)", caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber()),
                message, getStackTraceAsString(throwable)).trim();
        writer.println(msg);
        writer.flush();
    }
}
