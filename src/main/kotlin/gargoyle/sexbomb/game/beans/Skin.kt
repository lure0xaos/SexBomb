package gargoyle.sexbomb.game.beans

import gargoyle.sexbomb.util.ini.PropertyMap
import gargoyle.sexbomb.util.res.Res
import gargoyle.sexbomb.util.res.load.Loaders
import java.awt.Image
import java.io.IOException
import java.io.InputStreamReader
import java.io.Serializable
import java.net.URL
import java.nio.charset.Charset

class Skin(url: URL, charset: Charset) : Serializable {
    var closed: Image
    var flag: Image
    var mine: Image
    var mined: Image
    var name: String
    var noMine: Image
    var open0: Image
    var open1: Image
    var open2: Image
    var open3: Image
    var open4: Image
    var open5: Image
    var open6: Image
    var open7: Image
    var open8: Image
    var question: Image

    init {
        InputStreamReader(url.openStream(), charset).use {
            val file = PropertyMap(it)
            name = file[PROP_NAME]!!
            closed = image(url, file[PROP_CLOSED])
            flag = image(url, file[PROP_FLAG])
            mine = image(url, file[PROP_MINE])
            mined = image(url, file[PROP_MINED])
            noMine = image(url, file[PROP_NOMINE])
            open0 = image(url, file[PROP_OPEN_0])
            open1 = image(url, file[PROP_OPEN_1])
            open2 = image(url, file[PROP_OPEN_2])
            open3 = image(url, file[PROP_OPEN_3])
            open4 = image(url, file[PROP_OPEN_4])
            open5 = image(url, file[PROP_OPEN_5])
            open6 = image(url, file[PROP_OPEN_6])
            open7 = image(url, file[PROP_OPEN_7])
            open8 = image(url, file[PROP_OPEN_8])
            question = image(url, file[PROP_QUESTION])
        }
    }

    companion object {
        private const val PROP_CLOSED = "closed"
        private const val PROP_FLAG = "flag"
        private const val PROP_MINE = "mine"
        private const val PROP_MINED = "mined"
        private const val PROP_NAME = "name"
        private const val PROP_NOMINE = "nomine"
        private const val PROP_OPEN_0 = "open0"
        private const val PROP_OPEN_1 = "open1"
        private const val PROP_OPEN_2 = "open2"
        private const val PROP_OPEN_3 = "open3"
        private const val PROP_OPEN_4 = "open4"
        private const val PROP_OPEN_5 = "open5"
        private const val PROP_OPEN_6 = "open6"
        private const val PROP_OPEN_7 = "open7"
        private const val PROP_OPEN_8 = "open8"
        private const val PROP_QUESTION = "question"

        @Throws(IOException::class)
        private fun image(url: URL, name: String?): Image {
            return Loaders.load(Image::class.java, Res.url(url, name)!!)
        }
    }
}
