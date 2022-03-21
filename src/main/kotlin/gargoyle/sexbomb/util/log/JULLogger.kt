package gargoyle.sexbomb.util.log

import java.io.IOException
import java.util.Date
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.LogRecord

class JULLogger(level: LEVEL) : Logger(level) {
    override fun log(
        caller: StackTraceElement,
        dateTime: Long,
        level: LEVEL,
        message: String,
        throwable: Throwable?
    ) {
        String.format(
            "[%1\$tF %1\$tC] [%2\$s] %3\$s: %4\$s%n%5\$s",
            dateTime,
            level,
            caller,
            message,
            getStackTraceAsString(throwable)
        ).trim()
        val record = LogRecord(toLevel(level), message)
        record.instant = Date(dateTime).toInstant()
        record.sourceClassName =
            String.format("%s.%s(%s:%d)", caller.className, caller.methodName, caller.fileName, caller.lineNumber)
        record.thrown = throwable
        record.loggerName = caller.className
        java.util.logging.Logger.getLogger(caller.className).log(record)
    }

    companion object {
        init {
            try {
                LogManager.getLogManager()
                    .readConfiguration(JULLogger::class.java.getResourceAsStream("/logging.properties"))
            } catch (e: IOException) {
                throw RuntimeException(e.message, e)
            }
            //        SLF4JBridgeHandler.removeHandlersForRootLogger();
//        SLF4JBridgeHandler.install();
//        java.util.logging.Logger.getGlobal().setLevel(Level.FINEST);
//        java.util.logging.Logger.getLogger("").setLevel(Level.FINEST);
        }

        private fun toLevel(level: LEVEL?): Level {
            return when (level) {
                LEVEL.FATAL -> Level.SEVERE
                LEVEL.ERROR -> Level.SEVERE
                LEVEL.WARN -> Level.WARNING
                LEVEL.INFO -> Level.INFO
                LEVEL.DEBUG -> Level.FINE
                else -> Level.SEVERE
            }

        }
    }
}
