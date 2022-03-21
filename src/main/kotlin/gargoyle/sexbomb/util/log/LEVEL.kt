package gargoyle.sexbomb.util.log

enum class LEVEL(string: String, level: Int) {
    FATAL("FATAL", 0), ERROR("ERROR", 1), INFO("INFO", 2), WARN("WARN", 3), DEBUG("DEBUG", 4);

    private val level: Int
    private val string: String

    init {
        this.string = string
        this.level = level
    }

    fun canLog(level: LEVEL): Boolean {
        return this.level >= level.level
    }

    override fun toString(): String {
        return string
    }
}
