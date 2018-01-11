package gargoyle.sexbomb.util.log;

public enum LEVEL {
    FATAL("FATAL", 0),
    ERROR("ERROR", 1),
    INFO("INFO", 2),
    WARN("WARN", 3),
    DEBUG("DEBUG", 4);
    private final int level;
    private final String string;

    LEVEL(String string, int level) {
        this.string = string;
        this.level = level;
    }

    public boolean canLog(LEVEL level) {
        return this.level >= level.level;
    }

    @Override
    public String toString() {
        return string == null ? "" : string;
    }
}
