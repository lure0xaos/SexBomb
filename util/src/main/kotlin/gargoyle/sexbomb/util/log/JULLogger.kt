package gargoyle.sexbomb.util.log

import gargoyle.sexbomb.util.res.Resources
import java.util.*
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.LogRecord

class JULLogger(level: LEVEL) : Logger(level) {
    override fun log(caller: StackTraceElement, throwable: Throwable?, level: LEVEL, dateTime: Long, message: String) {
        LogRecord(
            when (level) {
                LEVEL.FATAL -> Level.SEVERE
                LEVEL.ERROR -> Level.SEVERE
                LEVEL.WARN -> Level.WARNING
                LEVEL.INFO -> Level.INFO
                LEVEL.DEBUG -> Level.FINE
            }, message
        ).apply {
            loggerName = caller.className
            instant = Date(dateTime).toInstant()
            sourceClassName = caller.className
            sourceMethodName = caller.methodName
            thrown = throwable
            java.util.logging.Logger.getLogger(caller.className).log(this)
        }
    }

    companion object {
        private const val CONFIG = "/logging.properties"

        init {
            Resources.resolveUrl(JULLogger::class, CONFIG)?.runCatching {
                openStream().use { it.apply { LogManager.getLogManager().readConfiguration(it) } }
            }
                ?.onFailure { SystemLogger.error("logger configuration error: ${it.localizedMessage}") }
                ?.onSuccess { SystemLogger.info("logger configured") }
                ?: SystemLogger.error("no logger config $CONFIG")
        }
    }
}
