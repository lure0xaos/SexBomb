package gargoyle.sexbomb.util.log

object Log {
    fun fatal(message: String): Unit =
        log(Logger.LEVEL.FATAL, message)

    fun fatal(throwable: Throwable, message: String? = null): Unit =
        log(throwable, Logger.LEVEL.FATAL, message)

    fun error(message: String): Unit =
        log(Logger.LEVEL.ERROR, message)

    fun error(throwable: Throwable, message: String? = null): Unit =
        log(throwable, Logger.LEVEL.ERROR, message)

    fun info(message: String): Unit =
        log(Logger.LEVEL.INFO, message)

    fun info(throwable: Throwable, message: String? = null): Unit =
        log(throwable, Logger.LEVEL.INFO, message)

    fun warn(message: String): Unit =
        log(Logger.LEVEL.WARN, message)

    fun warn(throwable: Throwable, message: String? = null): Unit =
        log(throwable, Logger.LEVEL.WARN, message)

    fun debug(message: String): Unit =
        log(Logger.LEVEL.DEBUG, message)

    fun debug(throwable: Throwable, message: String? = null): Unit =
        log(throwable, Logger.LEVEL.DEBUG, message)


    fun log(throwable: Throwable, level: Logger.LEVEL = Logger.LEVEL.DEBUG, message: String? = null): Unit =
        LoggerFactory.logger(level).apply {
            log(throwable, level, message)
        }.let { }

    fun log(level: Logger.LEVEL = Logger.LEVEL.DEBUG, message: String): Unit =
        LoggerFactory.logger(level).apply {
            log(level, message)
        }.let { }

}
