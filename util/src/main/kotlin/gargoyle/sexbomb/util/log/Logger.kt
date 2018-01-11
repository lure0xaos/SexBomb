package gargoyle.sexbomb.util.log

abstract class Logger(private val level: LEVEL) {

    fun fatal(message: String): Unit =
        log(LEVEL.FATAL, message)

    fun fatal(throwable: Throwable, message: String? = null): Unit =
        log(throwable, LEVEL.FATAL, message)

    fun error(message: String): Unit =
        log(LEVEL.ERROR, message)

    fun error(throwable: Throwable, message: String? = null): Unit =
        log(throwable, LEVEL.ERROR, message)

    fun info(message: String): Unit =
        log(LEVEL.INFO, message)

    fun info(throwable: Throwable, message: String? = null): Unit =
        log(throwable, LEVEL.INFO, message)

    fun warn(message: String): Unit =
        log(LEVEL.WARN, message)

    fun warn(throwable: Throwable, message: String? = null): Unit =
        log(throwable, LEVEL.WARN, message)

    fun debug(message: String): Unit =
        log(LEVEL.DEBUG, message)

    fun debug(throwable: Throwable, message: String? = null): Unit =
        log(throwable, LEVEL.DEBUG, message)

    fun log(level: LEVEL = LEVEL.DEBUG, message: String): Unit =
        log(getCaller(), null, level, System.currentTimeMillis(), message)

    fun log(throwable: Throwable, level: LEVEL = LEVEL.DEBUG, message: String? = null): Unit =
        log(
            getCaller(), throwable, level, System.currentTimeMillis(), message
                ?: "${throwable.let { it::class.qualifiedName } ?: ""}: ${throwable.localizedMessage ?: ""}\n${throwable.stackTraceToString()}")

    protected abstract fun log(
        caller: StackTraceElement,
        throwable: Throwable?,
        level: LEVEL,
        dateTime: Long,
        message: String
    )

    fun isLoggable(level: LEVEL): Boolean =
        level.isLoggable(this.level)

    companion object {
        private fun getCaller() = with(Log::class.java.getPackage().name) {
            Thread.currentThread().stackTrace
                .let { elements -> elements.drop(1).firstOrNull { !it.className.startsWith(this) } ?: elements.first() }
        }

    }

    enum class LEVEL(private val level: Int) {
        FATAL(0), ERROR(1), INFO(2), WARN(3), DEBUG(4);

        fun isLoggable(level: LEVEL): Boolean =
            this.level >= level.level

        override fun toString(): String =
            name
    }
}
