package gargoyle.sexbomb.util.log

object LoggerFactory {

    private val loggers = arrayOf<Logger>(JULLogger(Logger.LEVEL.DEBUG))

    fun logger(level: Logger.LEVEL = Logger.LEVEL.DEBUG): Logger =
        loggers.firstOrNull { it.isLoggable(level) } ?: SystemLogger
}
