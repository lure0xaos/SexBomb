package gargoyle.sexbomb.game.beans

import gargoyle.sexbomb.util.ini.PropertyMap
import gargoyle.sexbomb.util.log.Log
import gargoyle.sexbomb.util.res.Res
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset

class Level(url: URL) {
    var cover: URL? = null
    var height = 0
    var image: URL? = null
    var mines = 0
    var name: String? = null
    var width = 0

    init {
        try {
            InputStreamReader(url.openStream(), Charset.defaultCharset()).use { reader ->
                val file = PropertyMap(reader)
                name = file[PARAM_NAME]
                width = file[PARAM_WIDTH, DEFAULT_WIDTH]
                height = file[PARAM_HEIGHT, DEFAULT_HEIGHT]
                mines = file[PARAM_MINES, DEFAULT_MINES]
                cover = Res.url(url, file[PARAM_COVER])
                image = Res.url(url, file[PARAM_IMAGE])
            }
        } catch (e: IOException) {
            Log.error(String.format("cannot load campaign from %s", url), e)
        }
    }

    companion object {
        private const val DEFAULT_HEIGHT = 10
        private const val DEFAULT_MINES = 10
        private const val DEFAULT_WIDTH = 10
        private const val PARAM_COVER = "cover"
        private const val PARAM_HEIGHT = "height"
        private const val PARAM_IMAGE = "image"
        private const val PARAM_MINES = "mines"
        private const val PARAM_NAME = "name"
        private const val PARAM_WIDTH = "width"
    }
}
