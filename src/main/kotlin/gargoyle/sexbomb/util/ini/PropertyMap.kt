package gargoyle.sexbomb.util.ini

import gargoyle.sexbomb.util.log.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.Charset
import java.util.function.Function

class PropertyMap(reader: Reader) {
    private val entries: MutableMap<String, String> = mutableMapOf()

    constructor(stream: InputStream, charset: Charset) : this(InputStreamReader(stream, charset))

    init {
        try {
            BufferedReader(reader).use { bufferedReader -> load(bufferedReader) }
        } catch (e: IOException) {
            Log.error(e.localizedMessage, e)
        }
    }

    fun entrySet(): Set<Map.Entry<String, String?>> {
        return entries.entries
    }

    @JvmOverloads
    operator fun get(key: String, defaultValue: String? = null): String? {
        return entries[key] ?: defaultValue
    }

    operator fun get(key: String, defaultValue: Int): Int {
        return get(key, defaultValue.toString())!!.toInt()
    }

    operator fun get(key: String, defaultValue: Double): Double {
        return get(key, defaultValue.toString())!!.toDouble()
    }

    operator fun <T> get(key: String, defaultValue: T, parser: Function<String?, T>): T {
        return parser.apply(get(key, defaultValue.toString()))
    }

    @Throws(IOException::class)
    private fun load(reader: BufferedReader) {
        var line0: String? = null
        var line: String
        while (reader.readLine()?.also { line0 = it } != null) {
            line = line0!!.trim()
            if (line.isEmpty()) {
                continue
            }
            if (line.startsWith("#") || line.startsWith("//")) {
                continue
            }
            val i = line.indexOf('=')
            if (i > 0) {
                val key = line.substring(0, i).trim()
                val value = line.substring(i + 1).trim()
                put(key, value)
            }
        }
    }

    fun put(key: String, value: String) {
        entries[key] = value
    }
}
