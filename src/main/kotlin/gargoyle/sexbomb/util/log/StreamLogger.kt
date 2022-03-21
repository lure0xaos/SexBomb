package gargoyle.sexbomb.util.log

import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.Charset

abstract class StreamLogger private constructor(writer: Writer) : Logger(null) {
    private val writer: PrintWriter

    internal constructor() : this(Charset.defaultCharset())
    private constructor(charset: Charset) : this(OutputStreamWriter(System.err, charset))

    init {
        this.writer = PrintWriter(writer)
    }

    @Synchronized
    override fun log(
        caller: StackTraceElement,
        dateTime: Long,
        level: LEVEL,
        message: String,
        throwable: Throwable?
    ) {
        val msg = String.format(
            "[%1\$tF %1\$tC] [%2\$s] %3\$s: %4\$s%n%5\$s",
            dateTime,
            level,
            String.format("%s.%s(%s:%d)", caller.className, caller.methodName, caller.fileName, caller.lineNumber),
            message,
            getStackTraceAsString(throwable)
        ).trim()
        writer.println(msg)
        writer.flush()
    }
}
