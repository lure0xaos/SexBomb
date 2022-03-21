package gargoyle.sexbomb.util.log

import java.util.Date

object Log {
    private val LOGGERS = arrayOf<Logger>(
        JULLogger(LEVEL.DEBUG)
    )

    fun debug(message: String) {
        log(message, LEVEL.DEBUG)
    }

    fun error(message: String) {
        log(message, LEVEL.ERROR)
    }

    fun error(message: String?, throwable: Throwable) {
        log(message, throwable, LEVEL.ERROR)
    }

    fun fatal(message: String) {
        log(message, LEVEL.FATAL)
    }

    fun fatal(message: String?, throwable: Throwable) {
        log(message, throwable, LEVEL.FATAL)
    }

    private fun findLogger(level: LEVEL): Logger {
        for (logger in LOGGERS) {
            if (logger.canLog(level)) {
                return logger
            }
        }
        return SystemLogger.systemLogger
    }

    private val caller: StackTraceElement
        get() {
            val stackTrace = Thread.currentThread().stackTrace
            val name = Log::class.java.getPackage().name
            var i = 1
            val stackTraceLength = stackTrace.size
            while (i < stackTraceLength) {
                val element = stackTrace[i]
                val className = element.className
                if (!className.startsWith(name)) {
                    return element
                }
                i++
            }
            return stackTrace[0]
        }

    fun info(message: String) {
        log(message, LEVEL.INFO)
    }

    private fun log(message: String, level: LEVEL) {
        findLogger(level)
            .log(caller, Date().time, level, message, null)
    }

    fun log(message: String?, throwable: Throwable) {
        log(message, throwable, LEVEL.FATAL)
    }

    private fun log(message: String?, throwable: Throwable, level: LEVEL) {
        val logger = findLogger(level)
        logger.log(
            caller,
            Date().time,
            level,
            message ?: String.format("%s: %s", throwable.javaClass, throwable.localizedMessage),
            throwable
        )
    }

    fun warn(message: String) {
        log(message, LEVEL.WARN)
    }

    fun warn(msg: String?, throwable: Throwable) {
        log(msg, throwable, LEVEL.WARN)
    }
}
