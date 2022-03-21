package gargoyle.sexbomb.game.beans

import gargoyle.sexbomb.util.ini.PropertyMap
import gargoyle.sexbomb.util.log.Log
import gargoyle.sexbomb.util.res.Res
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset
import java.util.Collections

class Campaign(url: URL) {
    private val levels: MutableList<Level> = ArrayList()
    var name: String? = null

    init {
        try {
            InputStreamReader(url.openStream(), Charset.defaultCharset()).use { reader ->
                val file = PropertyMap(reader)
                name = file[PROP_NAME]
                val entries = file.entrySet()
                for ((key, value) in entries) {
                    if (PROP_NAME != key) {
                        val levelUrl = Res.url(url, value)
                        if (levelUrl != null) {
                            levels.add(Level(levelUrl))
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Log.error(String.format("cannot load campaign from %s", url), e)
        }
    }

    fun getLevels(): List<Level> {
        return Collections.unmodifiableList(levels)
    }

    fun getNextLevel(current: Level?): Level? {
        if (levels.isEmpty()) {
            return null
        }
        if (current == null) {
            return levels[0]
        }
        for (i in levels.indices) {
            val level = levels[i]
            if (level == current) {
                val next = i + 1
                return if (next == levels.size) null else levels[next]
            }
        }
        return null
    }

    companion object {
        private const val PROP_NAME = "name"
    }
}
