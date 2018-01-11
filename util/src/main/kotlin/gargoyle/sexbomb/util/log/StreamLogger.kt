package gargoyle.sexbomb.util.log

import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.Writer
import java.nio.charset.Charset

abstract class StreamLogger private constructor(writer: Writer) : Logger(LEVEL.DEBUG) {
    private val writer: PrintWriter

    internal constructor(charset: Charset = Charset.defaultCharset()) : this(OutputStreamWriter(System.err, charset))

    init {
        this.writer = PrintWriter(writer)
    }

    override fun log(
        caller: StackTraceElement,
        throwable: Throwable?,
        level: LEVEL,
        dateTime: Long,
        message: String
    ) {
        writer.println(
            String.format(
                "[%1\$tF %1\$tC] [%2\$s] %3\$s: %4\$s%n%5\$s",
                dateTime,
                level,
                "${caller.className}.${caller.methodName}(${caller.fileName}:${caller.lineNumber})",
                message,
                throwable?.stackTraceToString() ?: ""
            ).trim()
        )
        writer.flush()
    }
}
