package gargoyle.sexbomb.util.ini

import gargoyle.sexbomb.util.log.Log
import java.io.InputStream
import java.io.Reader
import java.nio.charset.Charset

class PropertyMap {
    private val entries: MutableMap<String, String>

    constructor(reader: Reader) {
        this.entries = runCatching { reader.readLines() }.onFailure { Log.error(it, it.localizedMessage) }.getOrThrow()
            .let { load(it).toMutableMap() }
    }

    constructor(stream: InputStream, charset: Charset) {
        this.entries =
            runCatching { stream.reader(charset).readLines() }.onFailure { Log.error(it, it.localizedMessage) }
                .getOrThrow().let { load(it).toMutableMap() }
    }

    fun entrySet(): Set<Map.Entry<String, String>> =
        entries.entries.map { (key, value) ->
            object : Map.Entry<String, String> {
                override val key: String
                    get() = key
                override val value: String
                    get() = value

            }
        }.toSortedSet { (key1), (key2) -> String.CASE_INSENSITIVE_ORDER.compare(key1, key2) }

    operator fun get(key: String): String? =
        entries[key]

    operator fun get(key: String, defaultValue: String = ""): String =
        entries[key] ?: defaultValue

    operator fun get(key: String, defaultValue: Int = 0): Int =
        entries[key]?.toInt() ?: defaultValue

    operator fun get(key: String, defaultValue: Double = 0.0): Double =
        entries[key]?.toDouble() ?: defaultValue

    operator fun <T : Any> get(key: String, defaultValue: T, parser: (String) -> T): T =
        entries[key]?.let { parser(it) } ?: defaultValue

    private fun load(lines: List<String>): Map<String, String> =
        lines.map { it.trim() }.filter { it.isNotBlank() && !it.startsWith("#") && !it.startsWith("//") }
            .associate { it.substringBefore('=').trim() to (it.substringAfter('=').trim()) }

    fun put(key: String, value: String) {
        entries[key] = value
    }
}
