package gargoyle.sexbomb.util.log

abstract class Logger(private val level: LEVEL?) {
    fun canLog(level: LEVEL?): Boolean {
        return this.level == null || level == null || level.canLog(this.level)
    }

    fun log(caller: StackTraceElement, date: Long, throwable: Throwable) {
        val message = getStackTraceAsString(throwable)
        log(
            caller,
            date,
            LEVEL.DEBUG,
            String.format("%s: %s\n%s", throwable.javaClass.name, throwable.localizedMessage, message),
            throwable
        )
    }

    abstract fun log(caller: StackTraceElement, dateTime: Long, level: LEVEL, message: String, throwable: Throwable?)

    companion object {
        fun getStackTraceAsString(throwable: Throwable?): String {
            return throwable?.stackTraceToString() ?: ""
        }
    }
}
